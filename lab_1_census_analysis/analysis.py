import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt

# Load data
df = pd.read_csv('data.csv')

# --- Lab 1 Analysis ---
print("--- Lab 1: Census Data Analysis ---")
print(f"Dataset Shape: {df.shape}")
print("\nDescriptive Statistics (Age, Hours):")
print(df[['age', 'hours-per-week']].describe())

# Pivot Table: Salary by Education and Sex
pivot = df.pivot_table(index='education', columns='sex', values='age', aggfunc='mean')
print("\nPivot Table (Mean Age by Education & Sex):")
print(pivot)

# Visualization
plt.figure(figsize=(10,6))
df['education'].value_counts().plot(kind='bar', color='teal')
plt.title('Education Distribution')
plt.tight_layout()
plt.savefig('education_dist.png')
print("\n[+] Chart saved: education_dist.png")
