#pragma once


// One dimensional vector of integers
class Vector1d {
    private:
        int m_size;
        int m_capacity;
        int* m_dynamicArray;
    public:
        Vector1d() {
            m_dynamicArray = new int[5];
            m_capacity = 5;
            m_size = 0;
        }
        // ~Vector1d() {
        //     delete[] m_dynamicArray;
        // }
        void resize(int newCapacity) {
            int* newDynamicArray = new int[newCapacity];

            for(int i = 0; i < m_capacity; i++) {
                *(newDynamicArray + i) = *(m_dynamicArray + i);
            }

            m_capacity = newCapacity;

            delete[] m_dynamicArray;
            m_dynamicArray = newDynamicArray;
        }

        void push_back(int value) {
            if(m_size >= m_capacity) {
                resize(m_capacity*2);
            }
            
            *(m_dynamicArray+m_size) = value;
            m_size++;
        }

        int size() {
            return m_size;
        }

        int operator[](int index) {
            return *(m_dynamicArray + index);
        }

        bool operator==(Vector1d vector) {
            if(m_size != vector.size()) {
                return false;
            }
            
            for(int i = 0; i < m_size; i++) {
                if(*(m_dynamicArray + i) != vector[i]) {
                    return false;
                }
            }

            return true;
        }

        bool contains(int value) {
            for(int i = 0; i < m_size; i++) {
                if(*(m_dynamicArray + i) == value) {
                    return true;
                }
            }
            return false;
        }

        void change(int index, int value) {
            *(m_dynamicArray + index) = value;
        }
};

// Two dimensional vector, consiting of one dimensional vectors
class Vector2d {
    private:
        int m_size;
        int m_capacity;
        Vector1d* m_dynamicArray;
    public:
        Vector2d() {
            m_dynamicArray = new Vector1d[5];
            m_capacity = 5;
            m_size = 0;
        }
        // ~Vector2d() {
        //     delete[] m_dynamicArray;
        // }
        void resize(int newCapacity) {
            Vector1d* newDynamicArray = new Vector1d[newCapacity];

            for(int i = 0; i < m_capacity; i++) {
                *(newDynamicArray + i) = *(m_dynamicArray + i);
            }

            m_capacity = newCapacity;

            delete[] m_dynamicArray;
            m_dynamicArray = newDynamicArray;
        }

        void push_back(Vector1d value) {
            if(m_size >= m_capacity) {
                resize(m_capacity*2);
            }
            
            *(m_dynamicArray+m_size) = value;
            m_size++;
        }

        int size() {
            return m_size;
        }

        Vector1d operator[](int index) {
            return *(m_dynamicArray + index);
        }

        bool contains(Vector1d vector) {
            for(int i = 0; i < m_size; i++) {
                if(*(m_dynamicArray + i) == vector) {
                    return true;
                }
            }
            return false;
        }

        int find(Vector1d vector) {
            for(int i = 0; i < m_size; i++) {
                if(*(m_dynamicArray + i) == vector) {
                    return i;
                }
            }
            return -1;
        }

        void change(int indexRow, int indexCol, int value) {
            (*(m_dynamicArray + indexRow)).change(indexCol, value);
        }
};

// Something like std::map but very specific, and very different inplementation. Like map
// it corresponds a key (in this case 1-d vector) to a value (in this case 2-d vector)
class VectorMap {
    private:
        int m_size;
        int m_capacity;
        Vector2d m_keys;
        Vector2d* m_dynamicValues;
    public:
        VectorMap() {
            m_dynamicValues = new Vector2d[5];
            m_capacity = 5;
            m_size = 0;
        }
        ~VectorMap() {
            delete[] m_dynamicValues;
        }

        void resize(int newCapacity) {
            Vector2d* newDynamicArray = new Vector2d[newCapacity];

            for(int i = 0; i < m_capacity; i++) {
                *(newDynamicArray + i) = *(m_dynamicValues + i);
            }

            m_capacity = newCapacity;

            delete[] m_dynamicValues;
            m_dynamicValues = newDynamicArray;
        }

        void append(Vector2d value) {
            if(m_size >= m_capacity) {
                resize(m_capacity*2);
            }
            
            *(m_dynamicValues+m_size) = value;
            m_size++;
        }

        bool keyExists(Vector1d vectorKey) {
            if(m_keys.contains(vectorKey)) {
                return true;
            } else {
                return false;
            }
        }

        Vector2d getValue(Vector1d vectorKey) {
            int index = m_keys.find(vectorKey);
            if(index == -1) {
                Vector2d emptyVector;
                return emptyVector;
            } else {
                return *(m_dynamicValues+index);
            }
        }

        void add(Vector1d vectorKey, Vector1d vectorValue) {
            int index = m_keys.find(vectorKey);
            if(index != -1) {
                (*(m_dynamicValues+index)).push_back(vectorValue);
            } else {
                m_keys.push_back(vectorKey);
                Vector2d newValue;
                newValue.push_back(vectorValue);
                append(newValue);
            }
        }
};