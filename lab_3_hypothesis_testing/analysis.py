import numpy as np
import scipy.stats as stats
import statsmodels.api as sm
from statsmodels.stats.weightstats import ztest as ztest
from statsmodels.stats.proportion import proportions_ztest

def print_result(name, stat, p_val, alpha=0.05):
    print(f"--- {name} ---")
    print(f"Statistic: {stat:.4f}")
    print(f"P-value: {p_val:.4f}")
    if p_val < alpha:
        print(f"Result: Reject Null Hypothesis (Significant difference at alpha={alpha})")
    else:
        print(f"Result: Fail to Reject Null Hypothesis (No significant difference at alpha={alpha})")
    print("\n")

# 1. One-Sample t-Test
delivery_times = [32, 28, 30, 29, 31, 35, 33, 27, 29, 28, 30, 34, 31, 29, 30]
t_stat, p_val = stats.ttest_1samp(delivery_times, 30)
print_result("1. One-Sample t-Test (Delivery Time)", t_stat, p_val)

# 2. Two-Sample (Independent) t-Test
trad_scores = [78, 85, 88, 92, 76, 80, 84, 75, 83, 89]
new_scores = [90, 88, 85, 93, 95, 87, 84, 91, 92, 90]
t_stat, p_val = stats.ttest_ind(trad_scores, new_scores)
print_result("2. Independent Two-Sample t-Test (Teaching Methods)", t_stat, p_val)

# 3. Paired t-Test
w_before = [70, 82, 85, 90, 88, 76, 95, 78, 84, 72, 80, 86]
w_after = [68, 80, 83, 85, 86, 74, 90, 76, 82, 70, 78, 85]
t_stat, p_val = stats.ttest_rel(w_before, w_after)
print_result("3. Paired t-Test (Diet Effect)", t_stat, p_val)

# 4. One-Sample t-Test with Normally Distributed Data
np.random.seed(42)
scores = np.random.normal(505, 10, 30) # Mean slightly off 500
t_stat, p_val = stats.ttest_1samp(scores, 500)
print_result("4. One-Sample t-Test (Generated Scores)", t_stat, p_val)

# 5. One-Sample z-Test
# Mean=9.5, Pop_Mean=10, std=1.2, n=50
# z = (x - mu) / (std / sqrt(n))
z_stat = (9.5 - 10) / (1.2 / np.sqrt(50))
p_val = stats.norm.sf(abs(z_stat)) * 2 # Two-tailed
print_result("5. One-Sample z-Test (Battery Life)", z_stat, p_val)

# 6. Two-Sample z-Test
# School A: n=80, m=75, s=10; School B: n=100, m=78, s=8
z_stat, p_val = ztest(x1=np.zeros(80), x2=np.zeros(100), value=75-78) # Manual calc is better here
# Manual: SE = sqrt(s1^2/n1 + s2^2/n2)
se = np.sqrt((10**2 / 80) + (8**2 / 100))
z_stat = (75 - 78) / se
p_val = stats.norm.sf(abs(z_stat)) * 2
print_result("6. Two-Sample z-Test (Exam Scores)", z_stat, p_val)

# 7. One-Sample z-Test for Proportion
# Claim: 60%, n=500, successes=290
stat, p_val = proportions_ztest(count=290, nobs=500, value=0.6)
print_result("7. One-Sample z-Test for Proportion (Shopping)", stat, p_val)

# 8. Two-Sample z-Test for Proportion
# City X: 250/1000, City Y: 320/1200
stat, p_val = proportions_ztest(count=[250, 320], nobs=[1000, 1200])
print_result("8. Two-Sample z-Test for Proportion (Transport)", stat, p_val)

# 9. ANOVA
only_breast = [794.1, 716.9, 993., 724.7, 760.9, 908.2, 659.3, 690.8, 768.7, 717.3, 630.7, 729.5, 714.1, 810.3, 583.5, 679.9, 865.1]
only_formula = [898.8, 881.2, 940.2, 966.2, 957.5, 1061.7, 1046.2, 980.4, 895.6, 919.7, 1074.1, 952.5, 796.3, 859.6, 871.1, 1047.5, 919.1, 1160.5, 996.9]
both = [976.4, 656.4, 861.2, 706.8, 718.5, 717.1, 759.8, 894.6, 867.6, 805.6, 765.4, 800.3, 789.9, 875.3, 740., 799.4, 790.3, 795.2, 823.6, 818.7, 926.8, 791.7, 948.3]
f_stat, p_val = stats.f_oneway(only_breast, only_formula, both)
print_result("9. ANOVA (Baby Weight Gain)", f_stat, p_val)

# 10. Chi-Squared Test
# Table construction (Gender x Risk)
# Assuming table data (since doc text was cut off, I will use a standard representation)
# Row 1: Men [Low, Med, High], Row 2: Women [Low, Med, High]
data = np.array([[120, 150, 60], [180, 100, 50]]) # Sample data
chi2, p_val, dof, ex = stats.chi2_contingency(data)
print_result("10. Chi-Squared Test (Gender vs Risk)", chi2, p_val)
