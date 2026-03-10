# Lab #5: Multidimensional and Text Analysis

## Objective
Apply Principal Component Analysis (PCA) for dimensionality reduction, perform Cluster Analysis (KMeans), and conduct Natural Language Processing (NLP) on textual data.

## Tasks & Methodology
### 1. Multidimensional Analysis (Titanic)
- **Features Used:** Pclass, Sex, Age, SibSp, Parch, Fare.
- **Scaling:** StandardScaler used for normalization.
- **PCA:** Reduced dimensions while maintaining 90% variance.
- **Clustering:** KMeans applied to PCA components to find underlying groupings.

### 2. Text Analysis
- **Pipeline:** Cleansing -> Tokenization -> Stopword Removal -> Stemming/Lemmatization.
- **Stemmers Compared:** Porter, Lancaster, Snowball.
- **Visualization:** Word Cloud and Frequency histograms to identify key themes.

## Key Findings
- **PCA:** Usually requires 5 components to explain >90% of the variance for the selected features.
- **Clustering:** The 2 clusters identified by KMeans often align closely with gender and class survival probabilities.
- **NLP:** Lemmatization provides more linguistically accurate roots compared to Stemming, which often chops off word endings aggressively.

## How to run
`python analysis.py`
