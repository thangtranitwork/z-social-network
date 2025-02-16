import re
from collections import Counter

from fastapi import FastAPI
from pydantic import BaseModel

# Khởi tạo FastAPI app
app = FastAPI()


# Định nghĩa model cho request body
class TextRequest(BaseModel):
    text: str
    num: int = 20
    max_words_per_phrase: int = 3  # Giới hạn số từ trong cụm từ khóa


# Hàm đọc file stopwords
def load_stopwords(file_path):
    with open(file_path, 'r', encoding='utf-8') as file:
        stopwords = set(line.strip() for line in file)
    print("Read ", len(stopwords), " stopwords")
    return stopwords


# Đọc file stopwords tiếng Việt và tiếng Anh
STOPWORDS = load_stopwords('stop_word.txt')


def split_text_by_stopwords(text, stopwords, max_words_per_phrase):
    text.replace("\r", "")
    text.replace("\n", "")
    """Split text into phrases by removing stopwords and separating punctuation."""
    # Tách các cụm từ bởi dấu câu
    text = re.sub(r'([.,;])', r' | ', text)

    # Sắp xếp stopwords theo độ dài giảm dần để xử lý các cụm nhiều từ trước
    sorted_stopwords = sorted(stopwords, key=len, reverse=True)
    stopword_pattern = re.compile(r"\b(" + "|".join(map(re.escape, sorted_stopwords)) + r")\b", re.IGNORECASE)

    # Thay thế stopwords bằng dấu |
    text = stopword_pattern.sub("|", text)

    # Tách câu theo dấu |
    phrases = text.split("|")

    # Loại bỏ các ký tự dư thừa như dấu ngoặc và khoảng trắng dư
    cleaned_phrases = [re.sub(r'[)\]}(\[{\'"]', '', phrase).strip() for phrase in phrases if phrase.strip()]

    # Giới hạn số từ trong mỗi cụm từ khóa
    limited_phrases = []
    for phrase in cleaned_phrases:
        words = phrase.split()
        if len(words) > max_words_per_phrase:
            limited_phrases.extend(
                [' '.join(words[i:i + max_words_per_phrase]) for i in range(0, len(words), max_words_per_phrase)])
        else:
            limited_phrases.append(phrase)

    return limited_phrases


def get_top_frequent_phrases(text, stopwords, num, max_words_per_phrase):
    phrases = split_text_by_stopwords(text, stopwords, max_words_per_phrase)
    phrase_counts = Counter(phrases)
    return set(phrase.lower() for phrase, _ in phrase_counts.most_common(num))


def extract_important_phrases(text):
    """Nhận diện các cụm từ quan trọng như viết hoa chữ cái đầu, toàn bộ từ viết hoa, số, và đoạn trong ngoặc (loại bỏ dấu ngoặc)."""
    pattern = re.compile(r'\b[A-Z][a-z]+\b|\b[A-Z]+\b|\b\d+\b|[(\[{\'"](.*?)[)\]}\'"]')
    matches = pattern.findall(text)
    cleaned_matches = [re.sub(r'[)\]}\'"]', '', match).strip().lower() for match in matches if match]
    return set(cleaned_matches)


@app.post("/extract-keywords-with-important-phrases")
def process_text(request: TextRequest):
    text = request.text
    num = request.num
    max_words_per_phrase = request.max_words_per_phrase
    stopwords = STOPWORDS
    frequent_words = get_top_frequent_phrases(text, stopwords, num, max_words_per_phrase)
    important_phrases = extract_important_phrases(text)
    result_set = frequent_words.union(important_phrases)

    return {"keywords": list(result_set)}


@app.post("/extract-keywords-only")
def process_text(request: TextRequest):
    text = request.text
    num = request.num
    max_words_per_phrase = request.max_words_per_phrase
    stopwords = STOPWORDS
    frequent_words = get_top_frequent_phrases(text, stopwords, num, max_words_per_phrase)

    return {"keywords": list(frequent_words)}


# Chạy ứng dụng
if __name__ == "__main__":
    import uvicorn

    uvicorn.run(app, host="0.0.0.0", port=8090)
# run command
# D:\Coding\z-social-network\keyword-extract-service\.venv\Scripts\python.exe -m uvicorn main:app --host 0.0.0.0 --port 8090 --reload
