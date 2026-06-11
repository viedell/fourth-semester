
import pandas as pd
import matplotlib.pyplot as plt

# Load dataset
df = pd.read_csv('LN11-creditcard.csv')

# Basic information
print("=== DATASET OVERVIEW ===")
print("Shape:", df.shape)
print("\nColumns:")
print(df.columns.tolist())

print("\n=== MISSING VALUES ===")
print(df.isnull().sum())

print("\n=== DUPLICATE ROWS ===")
print("Duplicates:", df.duplicated().sum())

print("\n=== STATISTICAL SUMMARY ===")
print(df.describe())

# Class distribution
print("\n=== CLASS DISTRIBUTION ===")
print(df['Class'].value_counts())

# Fraud percentage
fraud_percentage = (df['Class'].value_counts(normalize=True) * 100)
print("\nFraud Percentage:")
print(fraud_percentage)

# Transaction amount statistics
print("\n=== TRANSACTION AMOUNT STATS ===")
print(df['Amount'].describe())

# Visualization 1: Class distribution
plt.figure(figsize=(6,4))
df['Class'].value_counts().plot(kind='bar')
plt.title('Transaction Class Distribution')
plt.xlabel('Class (0 = Normal, 1 = Fraud)')
plt.ylabel('Count')
plt.show()

# Visualization 2: Transaction Amount
plt.figure(figsize=(6,4))
plt.hist(df['Amount'], bins=50)
plt.title('Transaction Amount Distribution')
plt.xlabel('Amount')
plt.ylabel('Frequency')
plt.show()

# Correlation matrix (top correlations with fraud)
correlation = df.corr()['Class'].sort_values(ascending=False)
print("\n=== TOP CORRELATIONS WITH CLASS ===")
print(correlation.head(10))
print(correlation.tail(10))
