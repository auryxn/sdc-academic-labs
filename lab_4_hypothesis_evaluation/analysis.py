import numpy as np
import scipy.stats as stats
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import os
from statsmodels.stats.weightstats import ztest as ztest
from statsmodels.stats.proportion import proportions_ztest

# Настройка путей
base_path = os.path.dirname(os.path.abspath(__file__))

def save_plot(name):
    plt.savefig(os.path.join(base_path, name))
    plt.close()

print("--- Lab 4: Hypothesis Evaluation (Full DOCX Compliance) ---")

# --- 1. One-Sample t-Test (Delivery Time) ---
# H0: mean = 30, H1: mean != 30
delivery_times = [32, 28, 30, 29, 31, 35, 33, 27, 29, 28, 30, 34, 31, 29, 30]
t_stat, p_val = stats.ttest_1samp(delivery_times, 30)
print(f"1. One-Sample t-Test: Stat={t_stat:.4f}, P={p_val:.4f}")

# --- 2. Two-Sample t-Test (Teaching Methods) ---
# H0: mean1 = mean2, H1: mean1 != mean2
trad = [78, 85, 88, 92, 76, 80, 84, 75, 83, 89]
new = [90, 88, 85, 93, 95, 87, 84, 91, 92, 90]
t_stat, p_val = stats.ttest_ind(trad, new)
print(f"2. Two-Sample t-Test: Stat={t_stat:.4f}, P={p_val:.4f}")

# --- 3. Paired t-Test (Diet Effect) ---
# H0: mean_diff = 0, H1: mean_diff != 0
w_before = [70, 82, 85, 90, 88, 76, 95, 78, 84, 72, 80, 86]
w_after = [68, 80, 83, 85, 86, 74, 90, 76, 82, 70, 78, 85]
t_stat, p_val = stats.ttest_rel(w_before, w_after)
print(f"3. Paired t-Test: Stat={t_stat:.4f}, P={p_val:.4f}")

# --- 4. One-Sample t-Test with generated normal data ---
np.random.seed(42)
scores = np.random.normal(505, 10, 30)
t_stat, p_val = stats.ttest_1samp(scores, 500)
print(f"4. One-Sample t-Test (Generated): Stat={t_stat:.4f}, P={p_val:.4f}")

# --- 5. One-Sample z-Test (mean) - Battery Life ---
# H0: mu=10, H1: mu!=10 (n=50, x_bar=9.5, std=1.2)
z_stat = (9.5 - 10) / (1.2 / np.sqrt(50))
p_val = stats.norm.sf(abs(z_stat)) * 2
print(f"5. One-Sample z-Test: Stat={z_stat:.4f}, P={p_val:.4f}")

# --- 6. Two-Sample z-Test (means) - Exam Scores ---
# School A (80, 75, 10), School B (100, 78, 8)
se = np.sqrt((10**2 / 80) + (8**2 / 100))
z_stat = (75 - 78) / se
p_val = stats.norm.sf(abs(z_stat)) * 2
print(f"6. Two-Sample z-Test: Stat={z_stat:.4f}, P={p_val:.4f}")

# --- 7. One-Sample z-Test (proportion) - Shopping ---
# Claim: 60%, n=500, successes=290
z_stat, p_val = proportions_ztest(count=290, nobs=500, value=0.6)
print(f"7. One-Sample z-Test (Prop): Stat={z_stat:.4f}, P={p_val:.4f}")

# --- 8. Two-Sample z-Test (proportions) - Transport ---
# City X: 250/1000, City Y: 320/1200
z_stat, p_val = proportions_ztest(count=[250, 320], nobs=[1000, 1200])
print(f"8. Two-Sample z-Test (Props): Stat={z_stat:.4f}, P={p_val:.4f}")

# --- 9. ANOVA test (compare 3 groups) - Baby Weight ---
only_breast = [794.1, 716.9, 993., 724.7, 760.9, 908.2, 659.3, 690.8, 768.7, 717.3, 630.7, 729.5, 714.1, 810.3, 583.5, 679.9, 865.1]
only_formula = [898.8, 881.2, 940.2, 966.2, 957.5, 1061.7, 1046.2, 980.4, 895.6, 919.7, 1074.1, 952.5, 796.3, 859.6, 871.1, 1047.5, 919.1, 1160.5, 996.9]
both = [976.4, 656.4, 861.2, 706.8, 718.5, 717.1, 759.8, 894.6, 867.6, 805.6, 765.4, 800.3, 789.9, 875.3, 740., 799.4, 790.3, 795.2, 823.6, 818.7, 926.8, 791.7, 948.3]
f_stat, p_val = stats.f_oneway(only_breast, only_formula, both)
print(f"9. ANOVA Test: Stat={f_stat:.4f}, P={p_val:.4f}")

# --- 10. Chi-square test (categorical independence) - Gender vs Risk ---
data_chi = np.array([[120, 150, 60], [180, 100, 50]]) 
chi2, p_val, dof, ex = stats.chi2_contingency(data_chi)
print(f"10. Chi-square Test: Stat={chi2:.4f}, P={p_val:.4f}")

# Visualizing Top 3 Key Tests for the Report
plt.figure(figsize=(10,6))
sns.boxplot(data=[trad, new], palette="Set2")
plt.xticks([0, 1], ['Traditional', 'New Method'])
plt.title('Teaching Methods Comparison (Two-Sample t-Test)')
save_plot('1_teaching_ttest.png')

plt.figure(figsize=(10,6))
sns.boxplot(data=[only_breast, only_formula, both], palette="pastel")
plt.xticks([0, 1, 2], ['Breast', 'Formula', 'Both'])
plt.title('Baby Weight Gain (ANOVA)')
save_plot('2_baby_anova.png')

plt.figure(figsize=(10,6))
sns.heatmap(data_chi, annot=True, fmt="d", cmap="Blues")
plt.title('Gender vs Risk (Chi-Square)')
save_plot('3_gender_chi2.png')

print("[+] Lab 4 complete. 10 Tests performed. Graphs saved.")
