### genre recognizer
import tensorflow as tf
from tensorflow.keras.models import load_model
from tensorflow.keras.optimizers import Adam
import numpy as np
import feature_extractor as fe
import time 
from enum import Enum
 
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
model = load_model(r'genre_recognizer\model\new_saved_model.h5')
end = time.time()

print("took " , (end - start))

# print(tf.__version__)

def predict_genre(song_path):

    feature_list = fe.get_features(song_path)

    input = np.array(feature_list)
    # Convert the data type of the array to float32
    input = input.astype(np.float32)
    # Reshape the array to match the expected input shape of the model
    input = np.reshape(input, (1, -1))

    prediction = model.predict(input)
    prediction_argmax = np.argmax(prediction)
    predicted_genre = genre(prediction_argmax).name

    return predicted_genre





if __name__ == "__main__":


    print(type(model))

    song_path = r"genre_recognizer\blues.00000.wav"
    prediction = predict_genre(song_path)
    print(prediction)
