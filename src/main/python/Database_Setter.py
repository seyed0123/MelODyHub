### database setterS
import psycopg2
import psycopg2.extras
import pandas as pd
import pickle
import traceback
from tqdm import tqdm

def set_songs_to_database():

    already_exist = [] # a list to collect the already exisint IDs

    # attributes for connecting to the database
    hostname = 'localhost'
    database = 'melodyhub'
    username = 'postgres'
    pwd = 'arshanelmtalab1398a'
    port_id = 5432
    conn = None

    # reading the csv file 
    df = pd.read_csv("tracks.csv")

    # connecting to the database
    try:
        with psycopg2.connect(
                    host = hostname,
                    dbname = database,
                    user = username,
                    password = pwd,
                    port = port_id) as conn:

            with conn.cursor(cursor_factory=psycopg2.extras.DictCursor) as cur: # for writing or reading from database

                # inseting value script
                insert_script  = 'INSERT INTO public.song(id , name , genre , duration , year) VALUES (%s , %s , %s , %s , %s)'

                # iterating through each row of the dataframe and extracting the necessary info
                for index , row in tqdm(df.iterrows()):

                    year = get_YearOfRelease(row["release_date"])
                    duration = get_duration(row["duration_ms"])
                    name = row["name"]
                    id = row["id"]
                    genre = "none"

                    insert_list = (str(id) , str(name) , genre , float(duration) , int(year)) # specifing the types , just to make sure ...

                    try :
                        # inserting each of the songs to the database 
                        cur.execute(insert_script , insert_list)
                        conn.commit()
                        # print(f"song id {id} inserted successfully!")

                    except psycopg2.IntegrityError: # if the value already exist in the database skip it
                        conn.rollback()
                        already_exist.append(id)
                        # print(f"Skipping song id {id} as it already exists!")

    except Exception as error:
        # print(error)
        traceback.print_exc()
    finally: # close the connection to the database
        if conn is not None:
            conn.close() 

    # dumping all of the songs that already exist in the database , might be usefull ...
    with open("already_exist_songs.pkl" , "wb") as file:
        pickle.dump(already_exist , file)

# turning the data type to int type of release year
def get_YearOfRelease(date):

    if "-" in date:
        release_year = date.split("-")[0]

    else : 
        release_year = date
    
    return release_year

# turning the duration in ms to minutes.seconds
def get_duration(duration):
    
    minutes = duration // 60000
    seconds = duration % 60000

    time = str(minutes) + "." + str(seconds)
    duration = float(time)

    return duration

if __name__ == "__main__":

    set_songs_to_database()
