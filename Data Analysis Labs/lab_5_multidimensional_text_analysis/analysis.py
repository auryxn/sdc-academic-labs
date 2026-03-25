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
nltk.download('punkt_tab', quiet=True)
nltk.download('stopwords', quiet=True)
nltk.download('wordnet', quiet=True)

# Настройка путей
base_path = os.path.dirname(os.path.abspath(__file__))

def save_plot(name):
    plt.savefig(os.path.join(base_path, name))
    plt.close()

print("--- Lab 5: PCA, Clustering, Text Analysis (Full DOCX Compliance) ---")

# --- 1. PCA ---
df = pd.read_csv(os.path.join(base_path, 'data.csv'))
df['Sex'] = df['Sex'].map({'male': 0, 'female': 1})
df['Age'] = df['Age'].fillna(df['Age'].median())
# Select relevant features
features = ['Pclass', 'Sex', 'Age', 'SibSp', 'Parch', 'Fare']
X = df[features]

# Scale data
scaler = StandardScaler()
X_scaled = scaler.fit_transform(X)

# Apply PCA
pca = PCA()
X_pca = pca.fit_transform(X_scaled)

# Choose number of components to explain at least 90% variance
cumulative_variance = np.cumsum(pca.explained_variance_ratio_)
n_components_90 = np.argmax(cumulative_variance >= 0.90) + 1
print(f"Components needed for 90% variance: {n_components_90}")

# Visualize first two principal components
plt.figure(figsize=(10,6))
plt.scatter(X_pca[:, 0], X_pca[:, 1], c=df['Survived'], cmap='coolwarm', alpha=0.6)
plt.title(f'PCA: First 2 Components (colored by Survived)')
plt.xlabel('PC1')
plt.ylabel('PC2')
plt.colorbar(label='Survived')
save_plot('1_pca_projection.png')

# --- 2. KMeans Clustering ---
# Apply KMeans (k=2) on PCA-reduced data (using first 2 PCs as per common lab practice)
kmeans = KMeans(n_clusters=2, random_state=42, n_init=10)
clusters = kmeans.fit_predict(X_pca[:, :2])

# Visualize clusters on 2D plot
plt.figure(figsize=(10,6))
plt.scatter(X_pca[:, 0], X_pca[:, 1], c=clusters, cmap='viridis', alpha=0.6)
plt.title('KMeans Clustering (k=2) on PCA Data')
plt.xlabel('PC1')
plt.ylabel('PC2')
save_plot('2_kmeans_clusters.png')

# --- 3. Text Analysis ---
text = """
The Titanic was a British passenger liner operated by the White Star Line that sank in the North Atlantic Ocean 
in the early morning hours of 15 April 1912, after it struck an iceberg during its maiden voyage from 
Southampton to New York City. Of the estimated 2,224 passengers and crew aboard, more than 1,500 died, 
making the sinking one of the deadliest commercial peacetime maritime disasters in modern history.
"""

# Cleaning & Tokenization
tokens = word_tokenize(text.lower())
tokens = [word for word in tokens if word.isalpha()]

# Removing stop words
stop_words = set(stopwords.words('english'))
filtered_tokens = [w for w in tokens if w not in stop_words]

# Stemming (Porter, Lancaster, Snowball)
porter = PorterStemmer()
lancaster = LancasterStemmer()
snowball = SnowballStemmer('english')

# Lemmatization (WordNet)
lemmatizer = WordNetLemmatizer()

print("\n--- Stemming vs Lemmatization Comparison ---")
for w in filtered_tokens[:5]:
    p = porter.stem(w)
    l = lancaster.stem(w)
    s = snowball.stem(w)
    lem = lemmatizer.lemmatize(w)
    print(f"Orig: {w:10} | Port: {p:10} | Lanc: {l:10} | Snow: {s:10} | Lem: {lem:10}")

# Word frequency analysis & Word cloud
lemmatized_list = [lemmatizer.lemmatize(w) for w in filtered_tokens]
wc = WordCloud(width=800, height=400, background_color='white').generate(" ".join(lemmatized_list))
plt.figure(figsize=(12,6))
plt.imshow(wc, interpolation='bilinear')
plt.axis('off')
plt.title('Word Cloud (Titanic Text Analysis)')
save_plot('3_wordcloud.png')

# Frequency Histogram
plt.figure(figsize=(10,6))
pd.Series(lemmatized_list).value_counts().head(10).plot(kind='bar', color='skyblue')
plt.title('Top 10 Most Common Terms (after cleaning)')
plt.ylabel('Frequency')
save_plot('4_text_frequency.png')

print("[+] Lab 5 complete. PCA, KMeans and NLP analysis performed. Graphs saved.")
