# --- Lab 4: Introduction to R (Iris and Mtcars) ---

# Load necessary libraries
# Note: In a headless environment, we focus on the logic and saving plots
library(ggplot2)
library(dplyr)

# 1. Load Datasets
data(iris)
data(mtcars)

# 2. Exploratory Data Analysis (EDA)
# 2.1 Iris
print("--- Iris Structure ---")
str(iris)
print("--- Iris Summary ---")
summary(iris)

# Visualizations (Iris)
png("iris_boxplot.png")
ggplot(iris, aes(x = Species, y = Sepal.Length)) +  
  geom_boxplot(fill = "lightblue") +  
  ggtitle("Boxplot of Sepal Length by Species") +  
  xlab("Species") +  
  ylab("Sepal Length (cm)")
dev.off()

# 2.2 Mtcars
print("--- Mtcars Structure ---")
str(mtcars)
print("--- Mtcars Summary ---")
summary(mtcars)

# Visualizations (Mtcars)
png("mtcars_scatter.png")
ggplot(mtcars, aes(x = hp, y = mpg)) +  
  geom_point(color = "blue") +  
  ggtitle("Miles per Gallon vs Horsepower") +  
  xlab("Horsepower") +  
  ylab("Miles per Gallon")
dev.off()

png("mtcars_bar.png")
mtcars %>%  
  group_by(cyl) %>%  
  summarise(average_mpg = mean(mpg)) %>%  
  ggplot(aes(x = as.factor(cyl), y = average_mpg)) +  
  geom_bar(stat = "identity", fill = "orange") +  
  ggtitle("Average MPG by Number of Cylinders") +  
  xlab("Number of Cylinders") +  
  ylab("Average MPG")
dev.off()

# 3. Statistical Tests
# 3.1 Iris ANOVA
print("--- ANOVA: Sepal Length ~ Species ---")
anova_results <- aov(Sepal.Length ~ Species, data = iris)  
print(summary(anova_results))

# 3.2 Mtcars Correlation
print("--- Correlation: HP vs MPG ---")
print(cor.test(mtcars, mtcars))
