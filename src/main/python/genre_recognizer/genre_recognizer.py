### genre recognizer
import tensorflow as tf
from tensorflow.keras.models import load_model
from tensorflow.keras.optimizers import Adam
import numpy as np
import feature_extractor as fe


# print(tf.__version__)





if __name__ == "__main__":

    model = load_model('model/new_saved_model.h5')

    print(type(model))

    song_path = "blues.00000.wav"
    feature_list = fe.get_features(song_path)
    input = np.array(feature_list)
    # Convert the data type of the array to float32
    input = input.astype(np.float32)

    # Reshape the array to match the expected input shape of the model
    input = np.reshape(input, (1, -1))
    print(input)
    print("\n")
    output = model.predict(input)
    print(output)
