from sklearn.metrics.pairwise import cosine_similarity

# Create user-item matrix
user_movie_matrix = movie_data.pivot_table(
    index='user_id',
    columns='title',
    values='rating'
)

# Fill NaN with 0
user_movie_matrix_filled = user_movie_matrix.fillna(0)

# Compute cosine similarity
user_similarity = cosine_similarity(user_movie_matrix_filled)

# Convert to DataFrame
user_similarity_df = pd.DataFrame(
    user_similarity,
    index=user_movie_matrix.index,
    columns=user_movie_matrix.index
)

# Recommendation function
def recommend_movies(user_id, n_recommendations=5):
    
    similar_users = user_similarity_df[user_id].sort_values(
        ascending=False
    )[1:11]
    
    similar_users_ratings = user_movie_matrix.loc[
        similar_users.index
    ].mean()
    
    watched_movies = user_movie_matrix.loc[user_id].dropna().index
    
    recommendations = similar_users_ratings.drop(
        watched_movies,
        errors='ignore'
    )
    
    recommendations = recommendations.sort_values(
        ascending=False
    )
    
    return recommendations.head(n_recommendations)

# Example user
recommendations = recommend_movies(50)

print("Recommendations for User 50:")
print(recommendations)