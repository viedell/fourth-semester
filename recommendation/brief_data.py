import matplotlib.pyplot as plt

# Number of users
num_users = movie_data['user_id'].nunique()

# Number of movies
num_movies = movie_data['movie_id'].nunique()

print("Number of users:", num_users)
print("Number of movies:", num_movies)

# Rating distribution
movie_data['rating'].hist()

plt.xlabel("Rating")
plt.ylabel("Count")
plt.title("Rating Distribution")
plt.show()

# Users with too few ratings
user_counts = movie_data.groupby('user_id')['rating'].count()

few_user = user_counts[user_counts < 5]

print("Users with very few ratings:")
print(few_user.head())

# Movies with too few ratings
movie_counts = movie_data.groupby('title')['rating'].count()

few_movies = movie_counts[movie_counts < 5]

print("Movies with very few ratings:")
print(few_movies.head())