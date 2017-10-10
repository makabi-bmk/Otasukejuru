from flask import request, Flask
from logging import getLogger, StreamHandler, DEBUG
import datetime as dt
import pytz
from serverapp import dbcon
from serverapp import strtime

logger = getLogger(__name__)
handler = StreamHandler()
handler.setLevel(DEBUG)
logger.setLevel(DEBUG)
logger.addHandler(handler)
logger.propagate = False

app = Flask(__name__)


def change_timezone(date: str, timezone='Asia/Tokyo') -> dt.datetime:
    if isinstance(date, str):
        return dt.datetime.strptime(date, '%Y-%m-%d %H:%M:%S').replace(
            tzinfo=pytz.timezone(timezone))
    elif isinstance(date, dt.datetime):
        return date.replace(tzinfo=pytz.timezone(timezone))
    else:
        logger.debug("change_timezone: arg is not dt.datetime instance")
        return None


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


# DBはMongoDBでいいのかな
@app.route('/add/task', methods=['POST'])
def add_task():
    if request.content_type != 'application/json':
        logger.debug('err invalid content_type. url: /add/task, content_type: '
                     '{}'.format(request.content_type))
        return 'failed'
    task_name = request.json['task_name']
    due_date = change_timezone(strtime.str_to_time(request.json['due_date']))
    task_type = request.json['task_type']
    guide_time = request.json['guide_time']
    progress = request.json['progress']
    priority = request.json['priority']
    dbcon.add_task(task_name, due_date, task_type, guide_time,
                   progress, priority)
    return 'succeeded'


@app.route('/add/schedule', methods=['POST'])
def add_schedule():
    if request.content_type != 'application/json':
        logger.debug('err invalid content_type. url: /add/schedule, '
                     'content_type: {}'.format(request.content_type))
        return 'failed'
    schedule_name = request.json['schedule_name']
    start_date = request.json['start_date']
    end_date = request.json['end_date']
    notice = request.json['notice']
    dbcon.add_schedule(schedule_name, start_date, end_date, notice)
    return 'succeeded'


@app.route('/get', methods=['POST'])
def get():
    pass


if __name__ == '__main__':
    # app.run(host='0.0.0.0', port=5000)
    app.run(debug=True, port=5000)
