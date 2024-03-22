import pandas as pd

# Load the dataset
df = pd.read_csv('src/main/resources/training-set/tcc_ceds_music.csv')

# Group by the 'genre' column
grouped = df.groupby('genre')

# Loop through the groups
for genre, group in grouped:
    # Define the file name based on the genre
    file_name = f'{genre}.csv'
    # Save each group to a separate file
    group.to_csv(file_name, index=False)

print("CSV files have been created based on distinct genres.")