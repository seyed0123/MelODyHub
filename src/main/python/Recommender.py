### recommender file
import pandas as pd
from pprint import pprint
import numpy as np 
from scipy.spatial.distance import cdist
import Receiver 

# getting the csv file 
df = Receiver.df

cols_to_cluster = ['key_1',
 'key_2',
 'key_3',
 'key_4',
 'key_5',
 'key_6',
 'key_7',
 'key_8',
 'key_9',
 'key_10',
 'key_11',
 'explicit_1',
 'mode_1',
 'time_signature_1',
 'time_signature_3',
 'time_signature_4',
 'time_signature_5',
 'popularity',
 'duration_ms',
 'danceability',
 'energy',
 'loudness',
 'speechiness',
 'acousticness',
 'instrumentalness',
 'liveness',
 'valence',
 'tempo']

def recommend(user_dict):

    id = user_dict["id"]
    recom_list = recom_by_song(user_dict)
    # artist_recom = recom_by_artist(user_dict)
    # mood_recom = recom_by_mood(user_dict)
    # genre_recom = recom_by_genre(user_dict)
    return id , recom_list


def recom_by_song(user_dict):

    recom_list = []

    songs = user_dict["listened_songs"]
    songs = np.array(songs)
    consistent_songs = sort_by_consistency(songs)

    most_listened = 10
    recom_num = 15

    # print("unqie_songs : " + str(consistent_songs.shape[0]))
    if consistent_songs.shape[0] > most_listened:
        consistent_songs = consistent_songs[0:most_listened+1]

    num_recoms = recom_num // consistent_songs.shape[0]
    left_num_recoms = recom_num % consistent_songs.shape[0]
    # print("num_recoms " + str(num_recoms) + "left_num_recoms" + str(left_num_recoms))

    for i , song in enumerate(consistent_songs):

        target_song = df[df["id"] == song] 
        target_cluster = target_song["cluster"].values[0]
        # print("\n")
        X = df[df["cluster"] == target_cluster]

        # Calculate the distances between the target point and points in the target cluster
        distances = cdist(target_song[cols_to_cluster], X[cols_to_cluster])

        # Find the index of the nearest point
        nearest_index = np.argsort(distances[0])

        # # # Retrieve the nearest point
        nearest_row = X.iloc[nearest_index].values

        if(i == 0):
            nearest_row = nearest_row[:num_recoms + left_num_recoms]

        else :
            nearest_row = nearest_row[:num_recoms]
    
        for row in nearest_row :
            recom_list.append(row[0])


    return recom_list

def sort_by_consistency(list):

    unique_values, counts = np.unique(list, return_counts=True)
    sorted_indices = np.argsort(counts)
    sorted = np.empty(len(counts) , dtype = object)

    for i , index , in enumerate(sorted_indices ) :
        sorted[i] = unique_values[index]

    return sorted[::-1]

# TODO : making the appropriate recommendation methods 


def recom_by_mood():
    pass

def recom_by_genre():
    pass

def recom_by_artist():
    pass


