from flask import request, Flask
from logging import getLogger, StreamHandler, DEBUG

from serverapp import dbcon

logger = getLogger(__name__)
handler = StreamHandler()
handler.setLevel(DEBUG)
logger.setLevel(DEBUG)
logger.addHandler(handler)
logger.propagate = False

app = Flask(__name__)


@app.route('/')
def hello_world():
    return 'Hello, World!'


@app.route('/test_form', methods=['POST'])
def test_form():
    # print(request.form['data'])
    print(request.content_type)
    for key, value in request.json.items():
        print("{}: {}".format(key, value))
    return 'ok'


# TODO タスク追加の処理を書く
# DBはMongoDBでいいのかな
@app.route('/add/task', methods=['POST'])
def add_task():
    if request.content_type != 'application/json':
        logger.debug('err invalid content_type. url: /add/task, content_type: '
                     '{}'.format(request.content_type))
        return 'failed'
    for key, value in request.json.items():
        print("{}: {}".format(key, value))
    return 'succeeded'


# TODO スケジュール追加の処理を書く
@app.route('/add/schedule', methods=['POST'])
def add_schedule():
    if request.content_type != 'application/json':
        logger.debug('err invalid content_type. url: /add/schedule, '
                     'content_type: {}'.format(request.content_type))
        return 'failed'
    for key, value in request.json.items():
        print("{}: {}".format(key, value))
    return 'succeeded'


# TODO 毎日の予定追加の処理を書く
@app.route('/add/every', methods=['POST'])
def add_schedule():
    if request.content_type != 'application/json':
        logger.debug('err invalid content_type. url: /add/every, '
                     'content_type: {}'.format(request.content_type))
        return 'failed'
    for key, value in request.json.items():
        print("{}: {}".format(key, value))
    return 'succeeded'


@app.route('/get', methods=['POST'])
def get():
    pass


if __name__ == '__main__':
    # app.run(host='0.0.0.0', port=5000)
    app.run(debug=True, port=5000)
