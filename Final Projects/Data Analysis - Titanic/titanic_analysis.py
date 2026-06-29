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

df = sns.load_dataset('titanic')
print(f"Dataset shape: {df.shape}")
print(df.head())
print(df.info())
print(df.describe())
print(f"Missing values:\n{df.isnull().sum()}")

# Unique values
for col in ['sex', 'embarked', 'class', 'who']:
    print(f"{col}: {df[col].nunique()} unique -> {df[col].unique()}")

# Basic stats
print(f"\nSurvival rate: {df['survived'].mean():.2%}")
print(f"Male/Female count:\n{df['sex'].value_counts()}")

# Histograms
fig, axes = plt.subplots(2, 2, figsize=(14, 10))
sns.histplot(df['age'].dropna(), bins=30, kde=True, ax=axes[0,0])
axes[0,0].set_title('Age Distribution')
sns.histplot(df['fare'], bins=30, kde=True, ax=axes[0,1])
axes[0,1].set_title('Fare Distribution')
sns.histplot(df['age'].dropna(), bins=15, kde=True, ax=axes[1,0], color='green')
sns.histplot(df['fare'], bins=50, kde=True, ax=axes[1,1], color='red')
plt.tight_layout()
plt.savefig('output/1_histograms.png', dpi=150)
plt.close()

# Box plots
fig, axes = plt.subplots(1, 2, figsize=(14, 6))
sns.boxplot(x='class', y='fare', data=df, ax=axes[0])
sns.boxplot(x='sex', y='age', data=df, ax=axes[1])
plt.tight_layout()
plt.savefig('output/2_boxplots.png', dpi=150)
plt.close()

# Quantiles
for col in ['age', 'fare']:
    q1 = df[col].quantile(0.25)
    q2 = df[col].quantile(0.50)
    q3 = df[col].quantile(0.75)
    iqr = q3 - q1
    outliers = df[(df[col] < q1 - 1.5*iqr) | (df[col] > q3 + 1.5*iqr)][col]
    print(f"{col}: Q1={q1:.2f}, Q2={q2:.2f}, Q3={q3:.2f}, outliers={len(outliers)} ({len(outliers)/df[col].notna().sum()*100:.1f}%)")

# Scatter plot
fig, axes = plt.subplots(1, 2, figsize=(14, 6))
sns.scatterplot(x='age', y='fare', hue='survived', data=df, alpha=0.6, ax=axes[0])
sns.scatterplot(x='age', y='fare', hue='sex', data=df, alpha=0.6, ax=axes[1])
plt.tight_layout()
plt.savefig('output/3_scatter.png', dpi=150)
plt.close()

# Violin plot
fig, axes = plt.subplots(1, 2, figsize=(14, 6))
sns.violinplot(x='class', y='age', hue='survived', data=df, split=True, ax=axes[0])
sns.violinplot(x='sex', y='fare', hue='survived', data=df, split=True, ax=axes[1])
plt.tight_layout()
plt.savefig('output/4_violin.png', dpi=150)
plt.close()

# Cat plot
g = sns.catplot(x='pclass', hue='survived', col='sex', data=df, kind='count', height=5)
g.savefig('output/5_catplot.png', dpi=150)
plt.close()

# Pie chart
fig, ax = plt.subplots()
counts = df['survived'].value_counts()
ax.pie(counts, labels=['Died', 'Survived'], autopct='%1.1f%%', colors=['#ff6b6b', '#51cf66'])
plt.savefig('output/6_pie.png', dpi=150)
plt.close()

# Correlation matrix
numeric = df.select_dtypes(include=[np.number])
corr = numeric.corr()
fig, ax = plt.subplots(figsize=(10, 8))
sns.heatmap(corr, annot=True, cmap='coolwarm', center=0)
plt.tight_layout()
plt.savefig('output/7_correlation_heatmap.png', dpi=150)
plt.close()
print(f"Correlation with survived:\n{corr['survived'].sort_values(ascending=False)}")

# Hypothesis tests
print("\n=== HYPOTHESIS TESTS ===")

# 1. One-Sample t-test: mean age = 30?
t, p = stats.ttest_1samp(df['age'].dropna(), 30)
print(f"1. Age = 30? t={t:.4f}, p={p:.4f}")

# 2. Two-Sample t-test: male age vs female age?
t, p = stats.ttest_ind(df[df['sex']=='male']['age'].dropna(), df[df['sex']=='female']['age'].dropna())
print(f"2. Male vs Female age? t={t:.4f}, p={p:.4f}")

# 3. Paired t-test: sibsp vs parch?
t, p = stats.ttest_rel(df['sibsp'], df['parch'])
print(f"3. Siblings vs Parents? t={t:.4f}, p={p:.4f}")

# 4. ANOVA: age by class
groups = [df[df['pclass']==i]['age'].dropna() for i in [1,2,3]]
f, p = stats.f_oneway(*groups)
print(f"4. Age by class? F={f:.4f}, p={p:.4f}")

# 5. Chi-squared: sex and survival
ct = pd.crosstab(df['sex'], df['survived'])
chi2, p, _, _ = stats.chi2_contingency(ct)
print(f"5. Sex vs Survived? chi2={chi2:.4f}, p={p:.4f}")

# 6. Chi-squared: class and survival
ct = pd.crosstab(df['pclass'], df['survived'])
chi2, p, _, _ = stats.chi2_contingency(ct)
print(f"6. Class vs Survived? chi2={chi2:.4f}, p={p:.4f}")

# 7. Two-Sample t-test: fare survived vs died?
t, p = stats.ttest_ind(df[df['survived']==1]['fare'], df[df['survived']==0]['fare'])
print(f"7. Fare: survived vs died? t={t:.4f}, p={p:.4f}")

# 8. Chi-squared: embark port and survival
ct = pd.crosstab(df['embarked'].dropna(), df.loc[df['embarked'].notna(), 'survived'])
chi2, p, _, _ = stats.chi2_contingency(ct)
print(f"8. Port vs Survived? chi2={chi2:.4f}, p={p:.4f}")

# 9. One-Sample t-test: mean fare = 50?
t, p = stats.ttest_1samp(df['fare'], 50)
print(f"9. Fare = $50? t={t:.4f}, p={p:.4f}")

# 10. One-Sample t-test: mean fare = 32?
t, p = stats.ttest_1samp(df['fare'], 32)
print(f"10. Fare = $32? t={t:.4f}, p={p:.4f}")

# PCA
print("\n=== PCA ===")
df_pca = df.copy()
df_pca['sex_num'] = (df_pca['sex'] == 'male').astype(int)
df_pca['embarked_num'] = df_pca['embarked'].map({'S': 0, 'C': 1, 'Q': 2})
features = ['pclass', 'sex_num', 'age', 'sibsp', 'parch', 'fare']
df_clean = df_pca[features].dropna()
print(f"Rows after cleaning: {len(df_clean)}")

scaler = StandardScaler()
scaled = scaler.fit_transform(df_clean)

pca = PCA()
pca_result = pca.fit_transform(scaled)
cumsum = np.cumsum(pca.explained_variance_ratio_)
n_comp = np.argmax(cumsum >= 0.90) + 1
print(f"Components needed for 90% variance: {n_comp}")
print(f"Explained variance: {pca.explained_variance_ratio_}")
print(f"Cumulative: {cumsum}")

fig, ax = plt.subplots(figsize=(10, 6))
ax.bar(range(1, len(pca.explained_variance_ratio_)+1), pca.explained_variance_ratio_, alpha=0.7)
ax.plot(range(1, len(cumsum)+1), cumsum, 'r-o')
ax.axhline(y=0.90, color='g', linestyle='--')
plt.savefig('output/8_pca_variance.png', dpi=150)
plt.close()

pca_2d = PCA(n_components=2)
reduced = pca_2d.fit_transform(scaled)
survived_vals = df.loc[df_clean.index, 'survived']

fig, ax = plt.subplots(figsize=(10, 8))
scatter = ax.scatter(reduced[:,0], reduced[:,1], c=survived_vals, cmap='coolwarm', alpha=0.6)
ax.set_xlabel('PC1')
ax.set_ylabel('PC2')
plt.savefig('output/9_pca_projection.png', dpi=150)
plt.close()

# KMeans
kmeans = KMeans(n_clusters=2, random_state=42, n_init=10)
clusters = kmeans.fit_predict(reduced)

fig, ax = plt.subplots(figsize=(10, 8))
scatter = ax.scatter(reduced[:,0], reduced[:,1], c=clusters, cmap='viridis', alpha=0.6)
ax.scatter(kmeans.cluster_centers_[:,0], kmeans.cluster_centers_[:,1], c='red', marker='X', s=200)
plt.savefig('output/10_kmeans_clusters.png', dpi=150)
plt.close()

cluster_df = pd.DataFrame({'cluster': clusters, 'survived': survived_vals.values})
print(f"\nCluster vs Survived:\n{pd.crosstab(cluster_df['cluster'], cluster_df['survived'])}")

print("\nDone. All charts saved to output/")
