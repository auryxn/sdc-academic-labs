import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from sklearn.decomposition import PCA
from sklearn.preprocessing import StandardScaler
from sklearn.cluster import KMeans
import nltk
from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize
from nltk.stem import PorterStemmer, WordNetLemmatizer
from wordcloud import WordCloud
import os

# Настройка путей
base_path = os.path.dirname(os.path.abspath(__file__))

def save_plot(name):
    plt.savefig(os.path.join(base_path, name))
    plt.close()

# Загрузка ресурсов NLTK
nltk.download('punkt', quiet=True)
nltk.download('punkt_tab', quiet=True)
nltk.download('stopwords', quiet=True)
nltk.download('wordnet', quiet=True)

print("--- Lab 5: Multidimensional & Text Analysis (Titanic) ---")

# --- ЧАСТЬ 1: МНОГОМЕРНЫЙ АНАЛИЗ (PCA & Clustering) ---
df = pd.read_csv(os.path.join(base_path, 'data.csv'))

# Подготовка: Pclass, Sex, Age, SibSp, Parch, Fare
df['Sex'] = df['Sex'].map({'male': 0, 'female': 1})
df['Age'] = df['Age'].fillna(df['Age'].median())
features = ['Pclass', 'Sex', 'Age', 'SibSp', 'Parch', 'Fare']
X = df[features]

# Масштабирование (Standardization)
scaler = StandardScaler()
X_scaled = scaler.fit_transform(X)

# 1. PCA: Снижение размерности
pca = PCA()
X_pca = pca.fit_transform(X_scaled)

# Кумулятивная дисперсия (Локоть)
plt.figure(figsize=(10,6))
plt.plot(np.cumsum(pca.explained_variance_ratio_), marker='o', linestyle='--', color='red')
plt.title('Кумулятивная объясненная дисперсия (PCA)')
plt.xlabel('Количество компонент')
plt.ylabel('Доля дисперсии')
plt.axhline(y=0.9, color='blue', linestyle='-')
plt.grid()
save_plot('1_pca_variance_elbow.png')

# 2. Визуализация в 2D (Первые две компоненты)
plt.figure(figsize=(10,6))
plt.scatter(X_pca[:, 0], X_pca[:, 1], c=df['Survived'], cmap='coolwarm', alpha=0.6)
plt.title('Проекция PCA (2 компоненты) по выживаемости')
plt.xlabel('PC1')
plt.ylabel('PC2')
plt.colorbar(label='Survived (1-Yes, 0-No)')
save_plot('2_pca_projection_2d.png')

# 3. Кластеризация KMeans
kmeans = KMeans(n_clusters=3, random_state=42, n_init=10)
df['Cluster'] = kmeans.fit_predict(X_scaled)

plt.figure(figsize=(10,6))
sns.scatterplot(x=X_pca[:, 0], y=X_pca[:, 1], hue=df['Cluster'], palette='viridis', alpha=0.7)
plt.title('Результаты кластеризации KMeans (K=3)')
save_plot('3_kmeans_clusters_pca.png')

# --- ЧАСТЬ 2: АНАЛИЗ ТЕКСТА (NLP) ---
text = """
The Titanic was a British passenger liner operated by the White Star Line that sank in the North Atlantic Ocean 
in the early morning hours of 15 April 1912, after it struck an iceberg during its maiden voyage from 
Southampton to New York City. Of the estimated 2,224 passengers and crew aboard, more than 1,500 died, 
making the sinking one of the deadliest commercial peacetime maritime disasters in modern history.
"""

# Очистка и Токенизация
tokens = word_tokenize(text.lower())
tokens = [word for word in tokens if word.isalpha()]

# Удаление стоп-слов
stop_words = set(stopwords.words('english'))
filtered_tokens = [w for w in tokens if w not in stop_words]

# Лемматизация
lemmatizer = WordNetLemmatizer()
lemmatized = [lemmatizer.lemmatize(w) for w in filtered_tokens]

# 4. Облако слов (WordCloud)
wc = WordCloud(width=800, height=400, background_color='white').generate(" ".join(lemmatized))
plt.figure(figsize=(12,6))
plt.imshow(wc, interpolation='bilinear')
plt.axis('off')
plt.title('Облако слов (Titanic History Text)')
save_plot('4_wordcloud_titanic.png')

# 5. Частотный анализ слов
plt.figure(figsize=(10,6))
pd.Series(lemmatized).value_counts().head(10).plot(kind='barh', color='teal')
plt.title('Топ-10 самых частых слов (после очистки)')
plt.gca().invert_yaxis()
save_plot('5_word_frequency_histogram.png')

print("[+] Лабораторная №5: Анализ завершен. Графики 1-5 сохранены.")
