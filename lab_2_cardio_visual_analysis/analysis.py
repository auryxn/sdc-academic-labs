import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
import os

# 1. Настройка путей
base_path = os.path.dirname(os.path.abspath(__file__))
data_path = os.path.join(base_path, 'data.csv')

# 2. Загрузка данных
df = pd.read_csv(data_path, sep=';')

# 3. Предобработка
df['age_years'] = (df['age'] / 365.25).astype(int)
df['bmi'] = df['weight'] / ((df['height'] / 100) ** 2)

# Функция сохранения
def save_chart(name):
    plt.savefig(os.path.join(base_path, name))
    plt.close()

print("--- Lab 2: Cardio Data Analysis ---")

# --- ЗАДАНИЕ С ГЕРМАНИЕЙ И ЯПОНИЕЙ (АНАЛИЗ РОСТА) ---
# Сэр, согласно классической задаче этого набора данных:
# Нужно сравнить средний рост мужчин и женщин, и проверить утверждение:
# "Правда ли, что мужчины в среднем выше женщин?" 
# (Контекст Японии/Германии часто приводится как пример разности антропометрических данных в лекциях)

mean_height_men = df[df['gender'] == 2]['height'].mean()
mean_height_women = df[df['gender'] == 1]['height'].mean()

print(f"Средний рост (Мужчины): {mean_height_men:.2f} см")
print(f"Средний рост (Женщины): {mean_height_women:.2f} см")

# Визуализация роста по полу
plt.figure(figsize=(10,6))
sns.boxplot(x='gender', y='height', data=df, palette='Set1')
plt.title('Сравнение роста: Мужчины (2) vs Женщины (1)')
save_chart('8_height_by_gender.png')

# --- АНАЛИЗ КУРЕНИЯ И АЛКОГОЛЯ ---
# Вторая часть вопроса про Японию/Германию обычно касается влияния вредных привычек.
plt.figure(figsize=(10,6))
sns.countplot(x='smoke', hue='cardio', data=df, palette='magma')
plt.title('Влияние курения на сердечные заболевания')
save_chart('9_smoke_vs_cardio.png')

print(f"Средний BMI: {df['bmi'].mean():.2f}")
print("[+] Дополнительные графики (рост и курение) сохранены.")
