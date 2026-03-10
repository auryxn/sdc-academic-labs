# --- Lab 5: Multidimensional Analysis (Titanic in R) ---
library(ggplot2)
library(dplyr)
library(stats)
library(cluster)

# 1. Load Data
df <- read.csv('data.csv')

# Preprocessing: Map Sex to numeric, fill Age NA
df$Sex <- as.numeric(factor(df$Sex, levels=c('male', 'female'))) - 1
df$Age[is.na(df$Age)] <- median(df$Age, na.rm=TRUE)

# Select features
features <- c('Pclass', 'Sex', 'Age', 'SibSp', 'Parch', 'Fare')
X <- df[, features]

# 2. PCA
X_scaled <- scale(X)
pca <- prcomp(X_scaled)

# Determine components for 90% variance
cumulative_variance <- cumsum(pca$sdev^2) / sum(pca$sdev^2)
n_components_90 <- which(cumulative_variance >= 0.90)[1]
print(paste("Minimum components for 90% variance (R):", n_components_90))

# Plot PCA Projection
png("pca_r.png")
plot_data <- as.data.frame(pca$x[, 1:2])
plot_data$Survived <- as.factor(df$Survived)
ggplot(plot_data, aes(x = PC1, y = PC2, color = Survived)) +
  geom_point(alpha = 0.6) +
  labs(title = "PCA Projection (R Implementation)") +
  theme_minimal()
dev.off()

# 3. Cluster Analysis (KMeans)
km <- kmeans(pca$x[, 1:n_components_90], centers = 2, nstart = 25)

png("clusters_r.png")
plot_data$Cluster <- as.factor(km$cluster)
ggplot(plot_data, aes(x = PC1, y = PC2, color = Cluster)) +
  geom_point(alpha = 0.6) +
  labs(title = "KMeans Clustering (PCA Components)") +
  theme_minimal()
dev.off()

print("[+] R plots generated: pca_r.png, clusters_r.png")
