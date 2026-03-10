import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt

# Load data
df = pd.read_csv('data.csv', sep=';')

# Basic Pre-processing
df['age_years'] = (df['age'] / 365.25).astype(int)
df['bmi'] = df['weight'] / ((df['height'] / 100) ** 2)

# Visualization: Cardio by Age
plt.figure(figsize=(12,6))
sns.countplot(x='age_years', hue='cardio', data=df)
plt.title('Cardio Disease Prevalence by Age')
plt.savefig('cardio_age_dist.png')

print("--- Lab 2: Cardio Data Analysis ---")
print(f"Average BMI: {df['bmi'].mean():.2f}")
print("[+] Plot saved: cardio_age_dist.png")
