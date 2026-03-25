import numpy as np
import scipy.stats as stats
import matplotlib.pyplot as plt
import seaborn as sns
import pandas as pd
import os

# Настройка путей
base_path = os.path.dirname(os.path.abspath(__file__))

def save_plot(name):
    plt.savefig(os.path.join(base_path, name))
    plt.close()

print("--- Lab 4: Hypothesis Testing (Visualization & Stats) ---")

# 1. СРАВНЕНИЕ СРЕДНИХ (t-Test) - Метод обучения
trad_scores = [78, 85, 88, 92, 76, 80, 84, 75, 83, 89]
new_scores = [90, 88, 85, 93, 95, 87, 84, 91, 92, 90]
t_stat, p_val = stats.ttest_ind(trad_scores, new_scores)

plt.figure(figsize=(10,6))
sns.kdeplot(trad_scores, fill=True, label='Traditional', color='blue')
sns.kdeplot(new_scores, fill=True, label='New Method', color='green')
plt.axvline(np.mean(trad_scores), color='blue', linestyle='--')
plt.axvline(np.mean(new_scores), color='green', linestyle='--')
plt.title(f'Сравнение методов обучения (p-value: {p_val:.4f})')
plt.legend()
save_plot('1_teaching_methods_ttest.png')

# 2. ПАРНЫЙ ТЕСТ (Paired t-Test) - Эффект диеты
w_before = [70, 82, 85, 90, 88, 76, 95, 78, 84, 72, 80, 86]
w_after = [68, 80, 83, 85, 86, 74, 90, 76, 82, 70, 78, 85]
data_paired = pd.DataFrame({'Before': w_before, 'After': w_after})

plt.figure(figsize=(10,6))
sns.boxplot(data=data_paired, palette="Set2")
plt.title('Эффект диеты: Вес До и После')
save_plot('2_diet_effect_paired.png')

# 3. ANOVA - Вес младенцев
only_breast = [794.1, 716.9, 993., 724.7, 760.9, 908.2, 659.3, 690.8, 768.7, 717.3, 630.7, 729.5, 714.1, 810.3, 583.5, 679.9, 865.1]
only_formula = [898.8, 881.2, 940.2, 966.2, 957.5, 1061.7, 1046.2, 980.4, 895.6, 919.7, 1074.1, 952.5, 796.3, 859.6, 871.1, 1047.5, 919.1, 1160.5, 996.9]
both = [976.4, 656.4, 861.2, 706.8, 718.5, 717.1, 759.8, 894.6, 867.6, 805.6, 765.4, 800.3, 789.9, 875.3, 740., 799.4, 790.3, 795.2, 823.6, 818.7, 926.8, 791.7, 948.3]
f_stat, p_val_anova = stats.f_oneway(only_breast, only_formula, both)

plt.figure(figsize=(10,6))
sns.boxplot(data=[only_breast, only_formula, both], palette="pastel")
plt.xticks([0, 1, 2], ['Breast', 'Formula', 'Both'])
plt.title(f'ANOVA: Вес по типу питания (p-value: {p_val_anova:.4f})')
save_plot('3_baby_weight_anova.png')

# 4. CHI-SQUARED - Пол vs Риск
data_chi = np.array([[120, 150, 60], [180, 100, 50]]) 
chi2, p_val_chi, dof, ex = stats.chi2_contingency(data_chi)

plt.figure(figsize=(10,6))
sns.heatmap(data_chi, annot=True, fmt="d", cmap="YlGnBu", 
            xticklabels=['Low', 'Medium', 'High'], yticklabels=['Men', 'Women'])
plt.title(f'Chi-Squared: Пол vs Риск (p-value: {p_val_chi:.4f})')
save_plot('4_gender_risk_chi2.png')

# 5. КОРРЕЛЯЦИЯ (Scatter + Stats)
np.random.seed(42)
x_cor = np.random.normal(100, 15, 50)
y_cor = 0.5 * x_cor + np.random.normal(0, 5, 50)
r, p_val_cor = stats.pearsonr(x_cor, y_cor)

plt.figure(figsize=(10,6))
sns.regplot(x=x_cor, y=y_cor, color='purple')
plt.title(f'Корреляция Пирсона (r: {r:.2f}, p: {p_val_cor:.4f})')
save_plot('5_pearson_correlation.png')

print("[+] Все статистические тесты выполнены, графики 1-5 сохранены.")
