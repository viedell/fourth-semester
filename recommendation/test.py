import pandas as pd
import numpy as np
import os

# Check current folder
print("Current Directory:", os.getcwd())

# Load ratings data
ratings = pd.read_csv(
    'ml-100k/u.data',
    sep='\t',
    names=['user_id', 'movie_id', 'rating', 'timestamp']
)

# Load movie data
movies = pd.read_csv(
    'ml-100k/u.item',
    sep='|',
    encoding='latin-1',
    header=None,
    usecols=[0, 1],
    names=['movie_id', 'title']
)

# Merge datasets
movie_data = pd.merge(ratings, movies, on='movie_id')

# Data cleaning
movie_data.drop_duplicates(inplace=True)
movie_data.dropna(inplace=True)

# Check result
print(movie_data.head())
print(movie_data.info())