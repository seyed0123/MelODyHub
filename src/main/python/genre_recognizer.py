### genre recognizer
import traceback
import tensorflow as tf
from tensorflow.keras.models import load_model
from tensorflow.keras.optimizers import Adam
import numpy as np
import feature_extractor as fe
import time 
from enum import Enum
import os
import librosa
import psycopg2
import psycopg2.extras
from pydub import AudioSegment
 
class genre(Enum):
    blues = 0
    classical = 1
    country = 2
    disco = 3
    hiphop = 4
    jazz = 5
    metal = 6
    pop = 7
    reggae = 8
    rock = 9

print("loading the model ")

start = time.time()
model = load_model(r'src\main\python\genre_recognizer\model\new_saved_model.h5')
end = time.time()

print("took " , (end - start))

# print(tf.__version__)

def predict_genre(audio , sr):

    feature_list = fe.get_features(audio , sr)

    input = np.array(feature_list)
    # Convert the data type of the array to float32
    input = input.astype(np.float32)
    # Reshape the array to match the expected input shape of the model
    input = np.reshape(input, (1, -1))

    prediction = model.predict(input)
    prediction_argmax = np.argmax(prediction)
    predicted_genre = genre(prediction_argmax).name

    return predicted_genre

def get_list_genre(recom_list):

    music_folder_path = r"C:\python\MACHINE LEARNING\recommendation_system\AP_project\front\new2_melody_hub\MelodyHub\src\main\resources\com\example\melodyhub\musics"
    # attributes for connecting to the database
    hostname = 'localhost'
    database = 'melodyhub'
    username = 'postgres'
    pwd = 'arshanelmtalab1398a'
    port_id = 5432
    conn = None

    for recom_id in recom_list:

        recom_mp3 = recom_id + ".mp3"
        recom_wav = recom_id + ".wav"
        music_path = os.path.join(music_folder_path , recom_mp3)
        # real_music_path = r"C:\python\MACHINE LEARNING\recommendation_system\AP_project\front\new2_melody_hub\MelodyHub\src\main\resources\com\example\melodyhub\musics\4M36kNFgfTRfssVd7EsAmA.mp3"


        # Files
        # src = "transcript.mp3"
        # dst = "test.wav"
        dst = os.path.join(music_folder_path , recom_wav)

        # Convert MP3 to WAV
        # sound = AudioSegment.from_mp3(music_path)
        # sound.export(dst, format="wav")

        music_path_wav = os.path.join(music_folder_path , recom_wav)
        # print("music path is : " , music_path_wav)

        # load and slice the song into 30 seconds
        sliced_song, sr = librosa.load(music_path_wav, sr=None, duration=30)
        print("the song has been sliced")

        prediction = predict_genre(sliced_song , sr)
        if prediction == "rock" :
            prediction = "pop"

    
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
                    insert_script  = 'UPDATE public.song SET genre=%s WHERE id = %s;'
                    
                    # inserting each recommendation song of each user id to the recommend_song table 
                    # for recom in recom_list:
                    cur.execute(insert_script, (prediction , recom_id))
                    conn.commit()
                    print("inserted genre " , prediction , " in id " , recom_id )

                    # cur.execute('SELECT * FROM public.recommend_song')
                    # print(cur.fetchall())

        except Exception as error:
            traceback.print_exc()
        finally: # close the connection to the database
            if conn is not None:
                conn.close() 



if __name__ == "__main__":


    # print(type(model))

    # song_path = r"genre_recognizer\blues.00000.wav"
    # prediction = predict_genre(song_path)
    # print(prediction)
    # C:\python\MACHINE LEARNING\recommendation_system\AP_project\front\new2_melody_hub\MelodyHub\src\main\resources\com\example\melodyhub\musics\3HxJaKzob7tdcr4qmqfR1d.wav

    recom_list = ['3HxJaKzob7tdcr4qmqfR1d']
    

    get_list_genre(recom_list)
