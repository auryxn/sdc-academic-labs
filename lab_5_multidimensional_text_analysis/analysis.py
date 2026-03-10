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
from nltk.stem import PorterStemmer, LancasterStemmer, SnowballStemmer, WordNetLemmatizer
from wordcloud import WordCloud
import os

# Download NLTK data
nltk.download('punkt', quiet=True)
nltk.download('stopwords', quiet=True)
nltk.download('wordnet', quiet=True)
nltk.download('omw-1.4', quiet=True)

# --- PART 1: PCA & Clustering (Titanic) ---
print("--- Part 1: PCA & Cluster Analysis (Titanic) ---")
df = pd.read_csv('data.csv')

# Preprocessing for Titanic
# Select features: Pclass, Sex, Age, SibSp, Parch, Fare
df['Sex'] = df['Sex'].map({'male': 0, 'female': 1})
df['Age'] = df['Age'].fillna(df['Age'].median())
features = ['Pclass', 'Sex', 'Age', 'SibSp', 'Parch', 'Fare']
X = df[features]

# Scale features
scaler = StandardScaler()
X_scaled = scaler.fit_transform(X)

# PCA
pca = PCA()
X_pca = pca.fit_transform(X_scaled)

# Determine components for 90% variance
cumulative_variance = np.cumsum(pca.explained_variance_ratio_)
n_components_90 = np.argmax(cumulative_variance >= 0.90) + 1
print(f"Minimum components for 90% variance: {n_components_90}")

# Plot PCA projection (first 2 components)
plt.figure(figsize=(10, 5))
plt.subplot(1, 2, 1)
plt.scatter(X_pca[:, 0], X_pca[:, 1], c=df['Survived'], cmap='viridis', alpha=0.6)
plt.title('PCA Projection (colored by Survived)')
plt.xlabel('PC1')
plt.ylabel('PC2')

# Clustering (KMeans with 2 clusters on PCA data)
kmeans = KMeans(n_clusters=2, random_state=42, n_init=10)
clusters = kmeans.fit_predict(X_pca[:, :n_components_90])

plt.subplot(1, 2, 2)
plt.scatter(X_pca[:, 0], X_pca[:, 1], c=clusters, cmap='tab10', alpha=0.6)
plt.title('KMeans Clustering (2 clusters)')
plt.xlabel('PC1')
plt.ylabel('PC2')
plt.savefig('pca_clusters.png')
print("[+] PCA and Clustering plots saved: pca_clusters.png")

# --- PART 2: Text Analysis ---
print("\n--- Part 2: Text Analysis ---")
text = """
The Titanic was a British passenger liner operated by the White Star Line that sank in the North Atlantic Ocean 
in the early morning hours of 15 April 1912, after it struck an iceberg during its maiden voyage from 
Southampton to New York City. Of the estimated 2,224 passengers and crew aboard, more than 1,500 died, 
making the sinking one of the deadliest commercial peacetime maritime disasters in modern history.
"""

# 1. Cleansing & Tokenization
tokens = word_tokenize(text.lower())
tokens = [word for word in tokens if word.isalpha()]

# 2. Removing Stopwords
stop_words = set(stopwords.words('english'))
filtered_tokens = [w for w in tokens if not w in stop_words]

# 3. Stemming Comparison
porter = PorterStemmer()
lancaster = LancasterStemmer()
snowball = SnowballStemmer('english')

print("Stemming Comparison (first 5 words):")
for w in filtered_tokens[:5]:
    print(f"Word: {w} | Porter: {porter.stem(w)} | Lancaster: {lancaster.stem(w)} | Snowball: {snowball.stem(w)}")

# 4. Lemmatization
lemmatizer = WordNetLemmatizer()
lemmatized = [lemmatizer.lemmatize(w) for w in filtered_tokens]
print("\nLemmatization (first 5 words):")
for i in range(5):
    print(f"Original: {filtered_tokens[i]} -> Lemmatized: {lemmatized[i]}")

# 5. Visualization (WordCloud)
wc = WordCloud(width=800, height=400, background_color='white').generate(" ".join(lemmatized))
plt.figure(figsize=(10, 5))
plt.imshow(wc, interpolation='bilinear')
plt.axis('off')
plt.title('Word Cloud of Titanic Text')
plt.savefig('wordcloud.png')

# 6. Word Frequency Histogram
plt.figure(figsize=(10, 5))
pd.Series(lemmatized).value_counts().head(10).plot(kind='bar', color='skyblue')
plt.title('Top 10 Most Common Terms')
plt.xticks(rotation=45)
plt.tight_layout()
plt.savefig('text_freq.png')
print("[+] Text analysis plots saved: wordcloud.png, text_freq.png")
