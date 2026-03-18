import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
import os

# 1. Авто-определение путей (чтобы работало на любом ПК)
base_path = os.path.dirname(os.path.abspath(__file__))
data_path = os.path.join(base_path, 'data.csv')

# Проверка наличия файла
if not os.path.exists(data_path):
    print(f"Ошибка: Не найден файл data.csv по пути: {data_path}")
    exit(1)

# 2. Загрузка данных
df = pd.read_csv(data_path)
sns.set_theme(style="whitegrid")

# --- Функция для сохранения графиков ---
def save_chart(name):
    path = os.path.join(base_path, name)
    plt.savefig(path)
    plt.close()
    print(f"[+] График сохранен: {path}")

# --- Анализ ---
print("--- Lab 1: Census Data Analysis ---")
print(f"Размер датасета: {df.shape}")

# 1. Распределение возраста (Гистограмма)
plt.figure(figsize=(10,6))
sns.histplot(df['age'], bins=20, kde=True, color='teal')
plt.title('Распределение возраста (Age Distribution)')
save_chart('1_age_dist.png')

# 2. Уровень образования (Count Plot)
plt.figure(figsize=(12,8))
sns.countplot(data=df, x='education', order=df['education'].value_counts().index, palette='viridis')
plt.title('Распределение уровней образования (Education Level)')
plt.xticks(rotation=45, ha='right')
plt.tight_layout()
save_chart('2_education_dist.png')

# 3. Средний возраст по полу (Bar Plot)
plt.figure(figsize=(8,6))
sns.barplot(data=df, x='sex', y='age', palette='coolwarm')
plt.title('Средний возраст по полу (Mean Age by Sex)')
save_chart('3_mean_age_sex.png')

# 4. Распределение рабочих часов в неделю (Box Plot)
plt.figure(figsize=(10,6))
sns.boxplot(data=df, x='hours-per-week', color='orange')
plt.title('Рабочие часы в неделю (Hours per Week Boxplot)')
save_chart('4_hours_boxplot.png')

# 5. Соотношение уровня образования и дохода (Count Plot by Salary)
plt.figure(figsize=(12,8))
sns.countplot(data=df, x='education', hue='salary', order=df['education'].value_counts().index, palette='magma')
plt.title('Доход в зависимости от образования (Salary vs Education)')
plt.xticks(rotation=45, ha='right')
plt.tight_layout()
save_chart('5_salary_education.png')

# 6. Соотношение расы и дохода
plt.figure(figsize=(10,6))
sns.countplot(data=df, x='race', hue='salary', palette='Set2')
plt.title('Доход в зависимости от расы (Salary vs Race)')
save_chart('6_salary_race.png')

# 7. Среднее количество рабочих часов по профессиям
plt.figure(figsize=(12,8))
sns.barplot(data=df, x='hours-per-week', y='occupation', palette='mako')
plt.title('Средние рабочие часы по профессиям (Mean Hours by Occupation)')
plt.tight_layout()
save_chart('7_hours_occupation.png')

# Вывод сводной таблицы (Бонус для защиты)
pivot = df.pivot_table(index='education', columns='sex', values='age', aggfunc='mean')
print("\nСводная таблица (Средний возраст по образованию):")
print(pivot)

print("\n[!] Все графики (7 штук) успешно сгенерированы в папку лабораторной.")
