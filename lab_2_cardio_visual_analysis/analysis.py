import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
import numpy as np
import os

# 1. Настройка путей
base_path = os.path.dirname(os.path.abspath(__file__))
data_path = os.path.join(base_path, 'data.csv')

# 2. Загрузка данных
df = pd.read_csv(data_path, sep=';')

# 3. ПОДГОТОВКА ДАННЫХ
# Возраст в годы + Индекс массы тела (BMI)
df['age_years'] = (df['age'] / 365.25).astype(int)
df['bmi'] = df['weight'] / ((df['height'] / 100) ** 2)

# Функция сохранения
def save_chart(name):
    plt.savefig(os.path.join(base_path, name))
    plt.close()

print("--- Lab 2: Cardio Data Analysis (Full Requirements) ---")

# 1. РАСПРЕДЕЛЕНИЯ (Гистограммы)
plt.figure(figsize=(10,6))
df['age_years'].hist(bins=30, color='skyblue', edgecolor='black')
plt.title('Распределение возраста (в годах)')
save_chart('1_age_hist.png')

# 2. ВЫБРОСЫ (Boxplot + Квартили)
plt.figure(figsize=(10,6))
sns.boxplot(x=df['height'], color='lightgreen')
plt.title('Поиск выбросов в росте (Boxplot)')
save_chart('2_height_boxplot.png')

# 3. СВЯЗИ (Scatter Plot)
# Посмотрим связь Роста и Веса (окраска по наличию болезни)
plt.figure(figsize=(10,6))
sns.scatterplot(x='height', y='weight', hue='cardio', data=df, alpha=0.3)
plt.title('Связь Роста и Веса (Scatter Plot)')
save_chart('3_height_weight_scatter.png')

# 4. СРАВНЕНИЕ ГРУПП (Violin Plot: Рост vs Пол)
plt.figure(figsize=(10,6))
sns.violinplot(x='gender', y='height', data=df, palette='muted')
plt.title('Сравнение роста по полу (1-Ж, 2-М)')
save_chart('4_height_gender_violin.png')

# 5. ВОЗРАСТ И БОЛЕЗНЬ (Countplot)
plt.figure(figsize=(12,6))
sns.countplot(x='age_years', hue='cardio', data=df)
plt.title('Количество больных по возрастам')
plt.xticks(rotation=90)
save_chart('5_age_cardio_count.png')

# 6. КТО БОЛЬШЕ ПЬЕТ? (Простой ответ)
# Группа 1 (Женщины), Группа 2 (Мужчины)
alco_by_gender = df.groupby('gender')['alco'].mean() * 100
print(f"Процент пьющих (Женщины): {alco_by_gender[1]:.2f}%")
print(f"Процент пьющих (Мужчины): {alco_by_gender[2]:.2f}%")

# 7. ГИПОТЕЗА: РИСК (Сравнение групп)
# Проверим: Влияет ли холестерин на риск заболевания?
chol_risk = df.groupby('cholesterol')['cardio'].mean() * 100
plt.figure(figsize=(10,6))
chol_risk.plot(kind='bar', color='salmon')
plt.title('Риск заболевания в зависимости от уровня холестерина')
plt.ylabel('% риска')
save_chart('6_cholesterol_risk_bar.png')

# 8. БАЛАНС ДАННЫХ (Catplot)
# Проверим, сбалансированы ли классы Cardio
g = sns.catplot(x="cardio", kind="count", data=df, palette="ch:.25")
g.fig.suptitle('Баланс целевой переменной (Cardio)')
g.savefig(os.path.join(base_path, '7_data_balance_catplot.png'))
plt.close()

print("--- Все пункты выполнены. Графики 1-7 сохранены. ---")
