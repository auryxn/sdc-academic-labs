# --- Lab 3: Introduction to R (Comprehensive Examples) ---
library(ggplot2)
library(dplyr)
library(tidyverse)

# 1. Basic Plotting (Simple Example)
x <- 1:10
y <- rnorm(10)
png("simple_plot.png")
plot(x, y, type = "b", col = "blue", pch = 19, main = "Example Plot", xlab = "X-axis", ylab = "Y-axis")
dev.off()

# 2. Iris Analysis with ggplot2
data(iris)
png("iris_scatter.png")
ggplot(data = iris, aes(x = Sepal.Length, y = Sepal.Width, color = Species)) +
  geom_point() +
  labs(title = "Sepal Length vs Sepal Width", x = "Sepal Length", y = "Sepal Width") +
  theme_minimal()
dev.off()

# 3. Tidyverse Data Processing (Without Setosa)
iris_tidy <- iris %>%
  select(Sepal.Length, Sepal.Width, Species) %>%
  filter(Species != "setosa")

png("iris_no_setosa.png")
ggplot(data = iris_tidy, aes(x = Sepal.Length, y = Sepal.Width, color = Species)) +
  geom_point() +
  labs(title = "Sepal Length vs Sepal Width (without setosa)", x = "Sepal Length", y = "Sepal Width") +
  theme_minimal()
dev.off()

# 4. Mtcars Analysis (Displacement vs Horsepower by Cylinders)
data(mtcars)
mtcars_tidy <- mtcars %>%
  select(mpg, cyl, disp, hp) %>%
  filter(cyl %in% c(4, 6, 8))

png("mtcars_cyl_disp.png")
ggplot(data = mtcars_tidy, aes(x = disp, y = hp, color = factor(cyl))) +
  geom_point() +
  labs(title = "Displacement vs Horsepower by Cylinders",
       x = "Displacement",
       y = "Horsepower")
dev.off()

# 5. Summary Statistics & Statistical Tests (from File 82)
print("--- Iris Summary ---")
summary(iris)

print("--- ANOVA: Sepal Length ~ Species ---")
anova_results <- aov(Sepal.Length ~ Species, data = iris)  
print(summary(anova_results))

print("--- Correlation: HP vs MPG ---")
print(cor.test(mtcars, mtcars))

print("[+] All plots generated: simple_plot.png, iris_scatter.png, iris_no_setosa.png, mtcars_cyl_disp.png")
