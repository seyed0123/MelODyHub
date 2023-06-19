### music genre feature extractor
import librosa
import numpy as np

def get_features(song_path):

    features_list = []

    # Load audio file
    audio, sr = librosa.load(song_path)

    # the length of the audio file
    length = 661794

    # Chroma STFT mean and variance
    chroma_stft = librosa.feature.chroma_stft(y=audio, sr=sr)
    chroma_stft_mean = np.mean(chroma_stft)
    chroma_stft_var = np.var(chroma_stft)

    # RMS mean and variance
    rms = librosa.feature.rms(y=audio)
    rms_mean = np.mean(rms)
    rms_var = np.var(rms)

    # Spectral centroid mean and variance
    spectral_centroid = librosa.feature.spectral_centroid(y=audio, sr=sr)
    spectral_centroid_mean = np.mean(spectral_centroid)
    spectral_centroid_var = np.var(spectral_centroid)

    # Spectral bandwidth mean and variance
    spectral_bandwidth = librosa.feature.spectral_bandwidth(y=audio, sr=sr)
    spectral_bandwidth_mean = np.mean(spectral_bandwidth)
    spectral_bandwidth_var = np.var(spectral_bandwidth)

    # Spectral rolloff mean and variance
    rolloff = librosa.feature.spectral_rolloff(y=audio, sr=sr)
    rolloff_mean = np.mean(rolloff)
    rolloff_var = np.var(rolloff)

    # Zero crossing rate mean and variance
    zero_crossing_rate = librosa.feature.zero_crossing_rate(y=audio)
    zcr_mean = np.mean(zero_crossing_rate)
    zcr_var = np.var(zero_crossing_rate)

    # Harmonic feature
    harmonic = librosa.effects.harmonic(y=audio)

    # Harmonic mean and variance
    harmonic_mean = np.mean(harmonic)
    harmonic_var = np.var(harmonic)

    # Perceptual loudness
    perceptual_loudness_mean = np.mean(rms[0])
    perceptual_loudness_var = np.var(rms[0])

    # Tempo estimation
    tempo, _ = librosa.beat.beat_track(y=audio, sr=sr)

    # MFCC 0-19 mean and variance
    mfcc = librosa.feature.mfcc(y=audio, sr=sr, n_mfcc=20)
    mfcc_list = []

    for i in range(20):
            
        mfcc_0_19_mean = np.mean(mfcc[i])  # Exclude the first coefficient (mfcc[0])
        mfcc_0_19_var = np.var(mfcc[i])
        mfcc_list.append(mfcc_0_19_mean)
        mfcc_list.append(mfcc_0_19_var)

    features_list.append(length)
    features_list.append(chroma_stft_mean)
    features_list.append(chroma_stft_var)
    features_list.append(rms_mean)
    features_list.append(rms_var)
    features_list.append(spectral_centroid_mean)
    features_list.append(spectral_centroid_var)
    features_list.append(spectral_bandwidth_mean)
    features_list.append(spectral_bandwidth_var)
    features_list.append(rolloff_mean)
    features_list.append(rolloff_var)
    features_list.append(zcr_mean)
    features_list.append(zcr_var)
    features_list.append(harmonic_mean)
    features_list.append(harmonic_var)
    features_list.append(perceptual_loudness_mean)
    features_list.append(perceptual_loudness_var)
    features_list.append(tempo)
    features_list += mfcc_list

    return features_list

















    # Print the extracted features
    # print("Chroma STFT Mean:", chroma_stft_mean)
    # print("Chroma STFT Variance:", chroma_stft_var)
    # print("RMS Mean:", rms_mean)
    # print("RMS Variance:", rms_var)
    # print("Perceptual Loudness_mean:", perceptual_loudness_mean)
    # print("Perceptual Loudness_var:", perceptual_loudness_var)
    # print("Spectral Centroid Mean:", spectral_centroid_mean)
    # print("Spectral Centroid Variance:", spectral_centroid_var)
    # print("Spectral Bandwidth Mean:", spectral_bandwidth_mean)
    # print("Spectral Bandwidth Variance:", spectral_bandwidth_var)
    # print("Spectral Rolloff Mean:", rolloff_mean)
    # print("Spectral Rolloff Variance:", rolloff_var)
    # print("Zero Crossing Rate Mean:", zcr_mean)
    # print("Zero Crossing Rate Variance:", zcr_var)
    # print("harmonic mean" , harmonic_mean)
    # print("harmonic var" , harmonic_var)
    # print("Tempo:", tempo)
    # print("mfcc list " , mfcc_list)
    # print("MFCC 0-19 Mean:", mfcc_0_19_mean)
    # print("MFCC 0-19 Variance:", mfcc_0_19_var)








if __name__ == '__main__':

    get_features("blues.00000.wav")









# length
# chroma stft mean , var
# rms mean , var 
# spectral_centroid_mean , spectral_centroid_var
# spectral_bandwidth_mean , spectral_bandwidth_var
# rollof_mean , rollof_var
# zero crossing rate mean , zero crossing rate var 
# harmony mean , harmony var 
# perceptr_mean , var
# tempo
# mffc 0_19 mean , var
