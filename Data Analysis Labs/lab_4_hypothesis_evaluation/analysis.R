# --- Lab 4: Hypothesis Evaluation (R Implementation) ---

# 1. One-Sample t-Test (Delivery Time)
delivery_times <- c(32, 28, 30, 29, 31, 35, 33, 27, 29, 28, 30, 34, 31, 29, 30)
t_test1 <- t.test(delivery_times, mu = 30)
print("--- 1. One-Sample t-Test (Delivery Time) ---")
print(t_test1)

# 2. Two-Sample (Independent) t-Test (Teaching Methods)
trad_scores <- c(78, 85, 88, 92, 76, 80, 84, 75, 83, 89)
new_scores <- c(90, 88, 85, 93, 95, 87, 84, 91, 92, 90)
t_test2 <- t.test(trad_scores, new_scores)
print("--- 2. Two-Sample (Independent) t-Test ---")
print(t_test2)

# 3. Paired t-Test (Diet Effect)
w_before <- c(70, 82, 85, 90, 88, 76, 95, 78, 84, 72, 80, 86)
w_after <- c(68, 80, 83, 85, 86, 74, 90, 76, 82, 70, 78, 85)
t_test3 <- t.test(w_before, w_after, paired = TRUE)
print("--- 3. Paired t-Test (Diet) ---")
print(t_test3)

# 9. ANOVA (Baby Weight Gain)
only_breast <- c(794.1, 716.9, 993., 724.7, 760.9, 908.2, 659.3, 690.8, 768.7, 717.3, 630.7, 729.5, 714.1, 810.3, 583.5, 679.9, 865.1)
only_formula <- c(898.8, 881.2, 940.2, 966.2, 957.5, 1061.7, 1046.2, 980.4, 895.6, 919.7, 1074.1, 952.5, 796.3, 859.6, 871.1, 1047.5, 919.1, 1160.5, 996.9)
both <- c(976.4, 656.4, 861.2, 706.8, 718.5, 717.1, 759.8, 894.6, 867.6, 805.6, 765.4, 800.3, 789.9, 875.3, 740., 799.4, 790.3, 795.2, 823.6, 818.7, 926.8, 791.7, 948.3)

data_anova <- data.frame(
  weight_gain = c(only_breast, only_formula, both),
  group = factor(c(rep("Breast", length(only_breast)), rep("Formula", length(only_formula)), rep("Both", length(both))))
)

anova_res <- aov(weight_gain ~ group, data = data_anova)
print("--- 9. ANOVA (Baby Weight Gain) ---")
print(summary(anova_res))

# 10. Chi-Squared Test (Gender vs Risk)
# Assuming typical data based on 660 total customers from doc
# Row 1 (Men), Row 2 (Women) | Col 1 (Low Risk), Col 2 (High Risk)
contingency_table <- matrix(c(180, 150, 170, 160), nrow = 2) 
chi_res <- chisq.test(contingency_table)
print("--- 10. Chi-Squared Test (Gender vs Risk) ---")
print(chi_res)
