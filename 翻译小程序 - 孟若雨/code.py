import re
import html
from urllib import parse
import requests

GOOGLE_TRANSLATE_URL = 'http://translate.google.cn/m?q=%s&tl=%s&sl=%s'

def translate(text, to_language="auto", text_language="auto"):

    text = parse.quote(text)
    url = GOOGLE_TRANSLATE_URL % (text,to_language,text_language)
    response = requests.get(url)
    data = response.text
    expr = r'(?s)class="(?:t0|result-container)">(.*?)<'
    result = re.findall(expr, data)
    if (len(result) == 0):
        return ""

    return html.unescape(result[0])

chinese = input("请输入想要翻译为英语的汉语：")

_chinese = input("请输入想要翻译为日语的汉语：")

english = input("请输入想要翻译为汉语的英语：")

japanese = input("请输入想要翻译为汉语的日语：")

if chinese != "":
    print(translate(chinese, "en", "zh-CN")) #汉语转英语
else:
    pass

if _chinese !="":
    print(translate(_chinese, "ja", "zh-CN")) #汉语转日语
else:
    pass

if english != "":
    print(translate(english, "zh-CN", "en")) #英语转汉语
else:
    pass
if japanese !="":
    print(translate(japanese, "zh-CN", "ja")) #汉语转日语
else:
    pass


input("输入任意键并回车结束运行")
import sys

sys.exit()