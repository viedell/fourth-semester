import pandas as pd
import numpy as np

# Load ratings data
ratings = pd.read_csv(
    'u.data',
    sep='\t',
    names=['user_id', 'movie_id', 'rating', 'timestamp']
)

# Load movie data
movies = pd.read_csv(
    'u.item',
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

# Display data
print(movie_data.head())