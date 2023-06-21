### receiver file
import numpy as np 
import pandas as pd
from itertools import groupby
import psycopg2
import psycopg2.extras
import traceback
import pickle
# reading the csv file containing the clustered songs
df = pd.read_csv("src\main\python\clustered_tracks.csv")
# print("read the track csv file")

# this function takes the table of the users history and turns each users
# history into a dictionary containing the age of the user , the id  and 
# the songs that the user has listened to 
def user_to_dict(table):

    users_dict = []
    users_list = [list(group) for key , group in groupby(table , key=lambda x: x[0])] # group by user 

    for user in users_list:
        listened_songs = []
        user_dict = {}

        # adding the age and the user id to the user dictionary 
        age = get_age(user[0][0]) # the user[0][0] code is the id of the user 
        user_dict["id"] = str(user[0][0])
        user_dict["age"] = age

        for song in user: # appending each song to the listened songs
            song_id = song[2]
            listened_songs.append(song_id)

        # adding a copy of listened songs to the user dictionary 
        # and and appending user_dict which is a dictionary to 
        # users_dict which is a list of all the users 
        user_dict["listened_songs"] = listened_songs.copy()
        users_dict.append(user_dict.copy())
        listened_songs.clear() # clearning the list to prevent collision
        
    return users_dict
    

# getting the table of the user histroy
def get_table():
    
    hostname = 'localhost'
    database = 'melodyhub'
    username = 'postgres'
    pwd = 'arshanelmtalab1398a'
    port_id = 5432
    conn = None

    try:
        with psycopg2.connect(
                    host = hostname,
                    dbname = database,
                    user = username,
                    password = pwd,
                    port = port_id) as conn:

            with conn.cursor(cursor_factory=psycopg2.extras.DictCursor) as cur:

                cur.execute('SELECT * FROM public.history')
                table = (cur.fetchall())
                condition = lambda x : x[1] == "listen"
                table = [x for x in table if condition(x)]


    except Exception as error:
        print(error)
    finally:
        if conn is not None:
            conn.close()

    return table

# by giving the id of the user the age of the user will be retreived
def get_age(id):

    hostname = 'localhost'
    database = 'melodyhub'
    username = 'postgres'
    pwd = 'arshanelmtalab1398a'
    port_id = 5432
    conn = None

    try:
        with psycopg2.connect(
                    host = hostname,
                    dbname = database,
                    user = username,
                    password = pwd,
                    port = port_id) as conn:

            with conn.cursor(cursor_factory=psycopg2.extras.DictCursor) as cur:

                cur.execute('SELECT * FROM public.person WHERE id= {}'.format(f"'{id}'"))
                age = (cur.fetchall())

                if age != None:
                    age = str(age[0][5])
                    age = 2023 - int(age.split("-")[0]) 


    except Exception as error:
        traceback.print_exc()
    finally:
        if conn is not None:
            conn.close()

    return age

# by giving the song id returns the song name
def get_song_name(id):

    song_info = df[df["id"] == id]
    song_name = song_info["name"].values[0]
    print(id)
    print(song_name)
    
    return song_name




#  TODO : completing the functions required for the program 

def check_condition(user):

    # user_dict = user


    with open(r'src\main\python\user_song_num.pickle', 'rb') as file:
        data = pickle.load(file)

    # go through all of the users that we have got from the receiver.user_to_dict() function
    # for user in user_dict:

    # store the each users id and listened songs and specify a boolean for if we find the user in the pickle data
    user_id = user["id"]
    listened_songs = user["listened_songs"]
    user_found_in_pickle = False

    # go through the dicts inside the data list and check all of the dictionaries
    for i , user_song_num in enumerate(data):

        # going through each dictionaries check if the id of the dict is the id of the user that we are checking for
        if user_song_num["id"] == user_id :

            # if you found a user in the one of the dicts store the song_nums of it and 
            # specify that we have found the user in our pickle file
            ls_num = user_song_num["song_num"]
            user_found_in_pickle = True

            # subtract the new listened songs from the song_nums of the pickle file 
            # if the result is bigger that or equal to 4 return true meaning that we now
            # want to give our user some recommendations
            if (len(listened_songs) - ls_num) >= 4:

                data[i]["song_num"] = len(listened_songs)
                with open(r'src\main\python\user_song_num.pickle', 'wb') as file:
                    pickle.dump(data , file)
                    
                return True
            
            else :
                return False
            
    if not user_found_in_pickle :

        user_song_num_dict = {}

        user_song_num_dict["id"] = str(user_id)
        user_song_num_dict["song_num"] = 0

        data.append(user_song_num_dict)

        with open(r'src\main\python\user_song_num.pickle', 'wb') as file:
            pickle.dump(data , file)

        return False




    return True

def organize_by_time():
    pass

def organize_by_user():
    pass

def connect_to_database():
    pass


if __name__ == '__main__':

    # table = get_table()
    # users_dict = user_to_dict(table)
    # print(users_dict)
    print(get_age("8kxHMpVt9OYDQOwXYJrQAb"))

