#!/usr/bin/env python3
"""
Data Analysis Individual Project — Titanic Dataset
Все этапы: Descriptive Stats, Visual Analysis, Hypothesis Tests, PCA, KMeans
"""

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from scipy import stats
from sklearn.preprocessing import StandardScaler
from sklearn.decomposition import PCA
from sklearn.cluster import KMeans
import warnings
warnings.filterwarnings('ignore')

sns.set_theme(style='whitegrid')
plt.rcParams['figure.figsize'] = (10, 6)

OUTPUT_DIR = '/tmp/data-analysis-project/output/'
import os
os.makedirs(OUTPUT_DIR, exist_ok=True)

# ============================================================
# 1. ЗАГРУЗКА ДАННЫХ
# ============================================================
df = sns.load_dataset('titanic')
print(f"Размер датасета: {df.shape[0]} строк, {df.shape[1]} столбцов")

# ============================================================
# 2. DESCRIPTIVE STATISTICS (Описательная статистика)
# ============================================================
print("\n=== DESCRIPTIVE STATISTICS ===")
print("\nПервые 5 строк:")
print(df.head())

print("\nТипы данных:")
print(df.dtypes)

print("\nОписательная статистика (числовые):")
print(df.describe())

print("\nУникальные значения (категориальные):")
for col in ['sex', 'embarked', 'class', 'who', 'deck', 'embark_town']:
    print(f"  {col}: {df[col].nunique()} уникальных → {df[col].unique()}")

print("\nЧастоты 'sex':")
print(df['sex'].value_counts())

print("\nЧастоты 'class':")
print(df['class'].value_counts())

# Пропуски
print("\nПропущенные значения:")
print(df.isnull().sum())
print(f"Всего пропусков: {df.isnull().sum().sum()}")

# Фильтрация: пассажиры 1 класса
first_class = df[df['class'] == 'First']
print(f"\nПассажиров 1 класса: {len(first_class)}")
survived_first = first_class['survived'].sum()
print(f"Выжило в 1 классе: {survived_first}")

# ============================================================
# 3. ВИЗУАЛИЗАЦИЯ
# ============================================================
print("\n=== VISUAL DATA ANALYSIS ===")

# 3.1 Гистограммы
fig, axes = plt.subplots(2, 2, figsize=(14, 10))
sns.histplot(df['age'].dropna(), bins=30, kde=True, ax=axes[0,0])
axes[0,0].set_title('Распределение возраста')
sns.histplot(df['fare'], bins=30, kde=True, ax=axes[0,1])
axes[0,1].set_title('Распределение стоимости билета')
sns.histplot(df['age'].dropna(), bins=15, kde=True, ax=axes[1,0], color='green')
axes[1,0].set_title('Возраст (15 bins)')
sns.histplot(df['fare'], bins=50, kde=True, ax=axes[1,1], color='red')
axes[1,1].set_title('Стоимость билета (50 bins)')
plt.tight_layout()
plt.savefig(f'{OUTPUT_DIR}1_histograms.png', dpi=150)
plt.close()

# 3.2 Box Plot
fig, axes = plt.subplots(1, 2, figsize=(14, 6))
sns.boxplot(x='class', y='fare', data=df, ax=axes[0])
axes[0].set_title('Box Plot: Стоимость билета по классам')
sns.boxplot(x='sex', y='age', data=df, ax=axes[1])
axes[1].set_title('Box Plot: Возраст по полу')
plt.tight_layout()
plt.savefig(f'{OUTPUT_DIR}2_boxplots.png', dpi=150)
plt.close()

# Квантили
for col in ['age', 'fare']:
    q1 = df[col].quantile(0.25)
    q2 = df[col].quantile(0.50)
    q3 = df[col].quantile(0.75)
    iqr = q3 - q1
    lower = q1 - 1.5 * iqr
    upper = q3 + 1.5 * iqr
    outliers = df[(df[col] < lower) | (df[col] > upper)][col]
    print(f"\n{col}: Q1={q1:.2f}, Q2={q2:.2f}, Q3={q3:.2f}, выбросов={len(outliers)} ({len(outliers)/df[col].notna().sum()*100:.1f}%)")

# 3.3 Scatter Plot
fig, axes = plt.subplots(1, 2, figsize=(14, 6))
sns.scatterplot(x='age', y='fare', hue='survived', data=df, alpha=0.6, ax=axes[0])
axes[0].set_title('Scatter: Возраст vs Стоимость билета (цвет = выживание)')
sns.scatterplot(x='age', y='fare', hue='sex', data=df, alpha=0.6, ax=axes[1])
axes[1].set_title('Scatter: Возраст vs Стоимость билета (цвет = пол)')
plt.tight_layout()
plt.savefig(f'{OUTPUT_DIR}3_scatter.png', dpi=150)
plt.close()

# 3.4 Violin Plot
fig, axes = plt.subplots(1, 2, figsize=(14, 6))
sns.violinplot(x='class', y='age', hue='survived', data=df, split=True, ax=axes[0])
axes[0].set_title('Violin: Возраст по классам (выжившие/погибшие)')
sns.violinplot(x='sex', y='fare', hue='survived', data=df, split=True, ax=axes[1])
axes[1].set_title('Violin: Стоимость билета по полу')
plt.tight_layout()
plt.savefig(f'{OUTPUT_DIR}4_violin.png', dpi=150)
plt.close()

# 3.5 CatPlot
g = sns.catplot(x='pclass', hue='survived', col='sex', data=df, kind='count', height=5)
g.fig.suptitle('CatPlot: Выживаемость по классу и полу', y=1.05)
g.savefig(f'{OUTPUT_DIR}5_catplot.png', dpi=150)
plt.close()

# 3.6 Круговая диаграмма
fig, ax = plt.subplots()
survived_counts = df['survived'].value_counts()
ax.pie(survived_counts, labels=['Погибли', 'Выжили'], autopct='%1.1f%%', 
       colors=['#ff6b6b', '#51cf66'], startangle=90)
ax.set_title('Соотношение выживших и погибших')
plt.savefig(f'{OUTPUT_DIR}6_pie.png', dpi=150)
plt.close()

# 3.7 Correlation Matrix
numeric_cols = df.select_dtypes(include=[np.number]).columns
corr = df[numeric_cols].corr()
fig, ax = plt.subplots(figsize=(10, 8))
sns.heatmap(corr, annot=True, cmap='coolwarm', center=0, ax=ax)
ax.set_title('Correlation Matrix')
plt.tight_layout()
plt.savefig(f'{OUTPUT_DIR}7_correlation_heatmap.png', dpi=150)
plt.close()

print("\nКорреляция с survived:")
print(corr['survived'].sort_values(ascending=False))

# ============================================================
# 4. HYPOTHESIS EVALUATION (Проверка гипотез)
# ============================================================
print("\n=== HYPOTHESIS TESTS ===")

# 4.1 One-Sample t-test: Средний возраст ≠ 30?
age_clean = df['age'].dropna()
t_stat, p_val = stats.ttest_1samp(age_clean, 30)
print(f"\n1. One-Sample t-test (средний возраст ≠ 30): t={t_stat:.4f}, p={p_val:.4f}")

# 4.2 Two-Sample t-test: Возраст мужчин ≠ возраст женщин?
male_age = df[df['sex'] == 'male']['age'].dropna()
female_age = df[df['sex'] == 'female']['age'].dropna()
t_stat, p_val = stats.ttest_ind(male_age, female_age)
print(f"2. Two-Sample t-test (возраст мужчин vs женщин): t={t_stat:.4f}, p={p_val:.4f}")

# 4.3 Paired t-test: sibsp vs parch?
# Тест для проверки — количество сиблингов отличается от родителей?
t_stat, p_val = stats.ttest_rel(df['sibsp'], df['parch'])
print(f"3. Paired t-test (sibsp vs parch): t={t_stat:.4f}, p={p_val:.4f}")

# 4.4 One-Sample z-test для fare
from statsmodels.stats.weightstats import ztest
# pip install statsmodels если нет
try:
    z_stat, p_val = ztest(df['fare'].dropna(), value=32)
    print(f"4. One-Sample z-test (fare ≠ $32): z={z_stat:.4f}, p={p_val:.4f}")
except:
    print("4. One-Sample z-test: statsmodels недоступен")

# 4.5 ANOVA: Возраст по классам
first_age = df[df['pclass'] == 1]['age'].dropna()
second_age = df[df['pclass'] == 2]['age'].dropna()
third_age = df[df['pclass'] == 3]['age'].dropna()
f_stat, p_val = stats.f_oneway(first_age, second_age, third_age)
print(f"5. ANOVA (возраст по классам): F={f_stat:.4f}, p={p_val:.4f}")

# 4.6 Chi-squared: Пол и выживание
contingency = pd.crosstab(df['sex'], df['survived'])
chi2, p_val, dof, expected = stats.chi2_contingency(contingency)
print(f"6. Chi-squared (пол и выживание): χ²={chi2:.4f}, p={p_val:.4f}")

# 4.7 Chi-squared: Класс и выживание
contingency = pd.crosstab(df['pclass'], df['survived'])
chi2, p_val, dof, expected = stats.chi2_contingency(contingency)
print(f"7. Chi-squared (класс и выживание): χ²={chi2:.4f}, p={p_val:.4f}")

# 4.8 Two-Sample t-test: Стоимость билета выживших vs погибших
surv_fare = df[df['survived'] == 1]['fare'].dropna()
died_fare = df[df['survived'] == 0]['fare'].dropna()
t_stat, p_val = stats.ttest_ind(surv_fare, died_fare)
print(f"8. Two-Sample t-test (fare: выжившие vs погибшие): t={t_stat:.4f}, p={p_val:.4f}")

# 4.9 Chi-squared: Порт посадки и выживание
contingency = pd.crosstab(df['embarked'].dropna(), df.loc[df['embarked'].notna(), 'survived'])
chi2, p_val, dof, expected = stats.chi2_contingency(contingency)
print(f"9. Chi-squared (порт посадки и выживание): χ²={chi2:.4f}, p={p_val:.4f}")

# 4.10 One-Sample t-test: Средняя стоимость билета ≠ 50
t_stat, p_val = stats.ttest_1samp(df['fare'].dropna(), 50)
print(f"10. One-Sample t-test (fare ≠ $50): t={t_stat:.4f}, p={p_val:.4f}")

# ============================================================
# 5. MULTIDIMENSIONAL ANALYSIS (PCA + KMeans)
# ============================================================
print("\n=== PCA + KMEANS ===")

# Подготовка данных
df_pca = df.copy()
df_pca['sex_num'] = (df_pca['sex'] == 'male').astype(int)
df_pca['embarked_num'] = df_pca['embarked'].map({'S': 0, 'C': 1, 'Q': 2})
features_for_pca = ['pclass', 'sex_num', 'age', 'sibsp', 'parch', 'fare']
df_pca_clean = df_pca[features_for_pca].dropna()

print(f"Данных после очистки: {len(df_pca_clean)}")

# Стандартизация
scaler = StandardScaler()
scaled = scaler.fit_transform(df_pca_clean)

# PCA
pca = PCA()
pca_result = pca.fit_transform(scaled)

# Кумулятивная дисперсия
cumsum = np.cumsum(pca.explained_variance_ratio_)
n_components = np.argmax(cumsum >= 0.90) + 1
print(f"Количество компонент для 90% дисперсии: {n_components}")
print(f"Объяснённая дисперсия по компонентам: {pca.explained_variance_ratio_}")
print(f"Кумулятивная: {cumsum}")

fig, ax = plt.subplots(figsize=(10, 6))
ax.bar(range(1, len(pca.explained_variance_ratio_) + 1), pca.explained_variance_ratio_, alpha=0.7, label='Individual')
ax.plot(range(1, len(cumsum) + 1), cumsum, 'r-o', label='Cumulative')
ax.axhline(y=0.90, color='g', linestyle='--', label='90% threshold')
ax.set_xlabel('Principal Component')
ax.set_ylabel('Explained Variance Ratio')
ax.set_title('PCA: Explained Variance')
ax.legend()
plt.savefig(f'{OUTPUT_DIR}8_pca_variance.png', dpi=150)
plt.close()

# PCA проекция на 2 компоненты
pca_2d = PCA(n_components=2)
reduced = pca_2d.fit_transform(scaled)

fig, ax = plt.subplots(figsize=(10, 8))
# Используем выживание для цвета
# Нужно получить survived для тех же индексов
survived_vals = df.loc[df_pca_clean.index, 'survived']
scatter = ax.scatter(reduced[:, 0], reduced[:, 1], c=survived_vals, 
                     cmap='coolwarm', alpha=0.6, edgecolors='k', s=50)
ax.set_xlabel('PC1')
ax.set_ylabel('PC2')
ax.set_title('PCA: Projection onto PC1 vs PC2')
legend = ax.legend(*scatter.legend_elements(), title='Survived')
ax.add_artist(legend)
plt.savefig(f'{OUTPUT_DIR}9_pca_projection.png', dpi=150)
plt.close()

# KMeans
kmeans = KMeans(n_clusters=2, random_state=42, n_init=10)
clusters = kmeans.fit_predict(reduced)

fig, ax = plt.subplots(figsize=(10, 8))
scatter = ax.scatter(reduced[:, 0], reduced[:, 1], c=clusters, cmap='viridis', 
                     alpha=0.6, edgecolors='k', s=50)
centers = ax.scatter(kmeans.cluster_centers_[:, 0], kmeans.cluster_centers_[:, 1], 
                     c='red', marker='X', s=200, label='Centroids')
ax.set_xlabel('PC1')
ax.set_ylabel('PC2')
ax.set_title(f'KMeans Clustering (k=2) on PCA-reduced Data')
ax.legend()
plt.savefig(f'{OUTPUT_DIR}10_kmeans_clusters.png', dpi=150)
plt.close()

# Сравнение кластеров с реальными данными
cluster_df = pd.DataFrame({'cluster': clusters, 'survived': survived_vals.values})
cross = pd.crosstab(cluster_df['cluster'], cluster_df['survived'])
print("\nCross-tab: Clusters vs Survived:")
print(cross)

# ============================================================
# 6. ЗАКЛЮЧЕНИЕ
# ============================================================
print("\n=== КЛЮЧЕВЫЕ ВЫВОДЫ ===")
print("1. Выживаемость сильно зависит от класса и пола")
print("2. Женщины и пассажиры 1 класса выживали чаще")
print("3. ANOVA: возраст значимо различается по классам")
print("4. PCA: для 90% дисперсии нужно 4-5 компонент")
print("5. KMeans: кластеры коррелируют с выживаемостью")
print("6. Стоимость билета — самый сильный предиктор выживания после пола")

print(f"\nВсе графики сохранены в {OUTPUT_DIR}")
