### receiver file
import numpy as np 
import pandas as pd
from itertools import groupby
import psycopg2
import psycopg2.extras
import io
import pprint

df = pd.read_csv("clustered_tracks.csv")
print("read the track csv file")

def check_condition():

    return True


def user_to_dict(table):

    users_dict = []


    users_list = [list(group) for key , group in groupby(table , key=lambda x: x[0])]

    for user in users_list:

        listened_songs = []
        user_dict = {}

        print(user[0][0])
        # user_id = user[0][0]
        age = get_age(user[0][0])
        user_dict["id"] = str(user[0][0])
        user_dict["age"] = age

        for song in user:
            song_id = song[2]
            # song_name = get_song_name(song_id)
            listened_songs.append(song_id)

        user_dict["listened_songs"] = listened_songs.copy()
        users_dict.append(user_dict.copy())
        listened_songs.clear()
        # print(users_dict)
        


    # user_dict = [{"id" : "13lk14h15l1h5lh" , "listened_songs" : ["It's Your Love" , "Ich lebe - Radio Version" , "You Keep It All In" , "Came in On My Own" , "Hurting Inside" , "It's Your Love" , "It's Your Love" , "It's Your Love" , "Ich lebe - Radio Version"] , "age" : 20 }]
    return users_dict
    


def get_table():
    
    hostname = 'localhost'
    database = 'melodyhub_new'
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


    except Exception as error:
        print(error)
    finally:
        if conn is not None:
            conn.close()

    return table

def get_age(id):

    hostname = 'localhost'
    database = 'melodyhub_new'
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
                age = age[0][5]


    except Exception as error:
        print(error)
    finally:
        if conn is not None:
            conn.close()

    return age

def get_song_name(id):

    song_info = df[df["id"] == id]
    song_name = song_info["name"].values[0]
    print(id)
    print(song_name)
    
    return song_name

#  TODO : completing the function required for the program 

def organize_by_time():
    pass

def organize_by_user():
    pass

def connect_to_database():
    pass


if __name__ == '__main__':

    table = get_table()
    users_dict = user_to_dict(table)
    # age = get_age("6kxHMpVt9OYDQOwXYJrQAb")
    # song_info = get_song_name("6kxHMpVt9OYDQOwXYJrQAb")
    # Read the output and decode it

    print(users_dict)

    # print(users_dict)
