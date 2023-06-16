### Databse_Pusher
import psycopg2
import psycopg2.extras


def push_to_database(user_id , recom_list):

    # attributes for connecting to the database
    hostname = 'localhost'
    database = 'melodyhub_new'
    username = 'postgres'
    pwd = 'arshanelmtalab1398a'
    port_id = 5432
    conn = None

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
                insert_script  = 'INSERT INTO public.recommend_song(userid , songid) VALUES (%s , %s)'
                
                # inserting each recommendation song of each user id to the recommend_song table 
                for recom in recom_list:
                    cur.execute(insert_script, (user_id , recom))
                    conn.commit()

                # cur.execute('SELECT * FROM public.recommend_song')
                # print(cur.fetchall())

    except Exception as error:
        print(error)
    finally: # close the connection to the database
        if conn is not None:
            conn.close() 

    

if __name__ == '__main__':

    insert_values = []
    user_id = ""
    push_to_database(user_id , insert_values)
