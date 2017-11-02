from flask import request, Flask, send_from_directory
from werkzeug.utils import secure_filename
from logging import getLogger, StreamHandler, DEBUG
import datetime as dt
import os
import json
import pytz
# from serverapp import dbcon
# from serverapp import strtime
import dbcon
import strtime

logger = getLogger(__name__)
handler = StreamHandler()
handler.setLevel(DEBUG)
logger.setLevel(DEBUG)
logger.addHandler(handler)
logger.propagate = False

app = Flask(__name__)

UPLOAD_FOLDER = './uploads'
ALLOWED_EXTENSIONS = ['png', 'jpeg', 'jpg', 'txt', 'gif']
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
app.config['SECRET_KEY'] = os.urandom(24)

friend_flag = False


def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1] in ALLOWED_EXTENSIONS


@app.route('/send', methods=['POST'])
def send():
    file = request.files['file']
    if file and allowed_file(file.filename):
        filename = secure_filename(file.filename)
        file.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))
        return 'succeed'
    else:
        return 'failed'


@app.route('/uploads/<filename>')
def uploads(filename=None):
    return send_from_directory(app.config['UPLOAD_FOLDER'], filename)


def change_timezone(date: str, timezone='Asia/Tokyo') -> dt.datetime:
    if isinstance(date, str):
        return strtime.str_to_datetime(date)# .replace(
            # tzinfo=pytz.timezone(timezone))
    elif isinstance(date, dt.datetime):
        return date# .replace(tzinfo=pytz.timezone(timezone))
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
    # print(request.content_type)
    if request.content_type != 'application/json; charset=utf-8':
        logger.debug('err invalid content_type. url: /add/task, content_type: '
                     '{}'.format(request.content_type))
        return 'failed'
    task_name = request.json['task_name']
    # print("due_date", request.json['due_date'])
    due_date = change_timezone(request.json['due_date'])
    task_type = request.json['task_type']
    # print('guide_time', request.json['guide_time'])
    guide_time = strtime.str_to_time(request.json['guide_time'])
    progress = request.json['progress']
    priority = request.json['priority']
    dbcon.add_task(task_name, due_date, task_type, guide_time,
                   progress, priority)
    return 'succeeded'


@app.route('/add/schedule', methods=['POST'])
def add_schedule():
    if request.content_type != 'application/json; charset=utf-8':
        logger.debug('err invalid content_type. url: /add/schedule, '
                     'content_type: {}'.format(request.content_type))
        return 'failed'
    schedule_name = request.json['schedule_name']
    start_date = change_timezone(request.json['start_time'])
    end_date = change_timezone(request.json['end_time'])
    # notice = request.json['notice']
    dbcon.add_schedule(schedule_name, start_date, end_date)
    return 'succeeded'


@app.route('/add/every', methods=['POST'])
def add_every():
    if request.content_type != 'application/json; charset=utf-8':
        logger.debug('err invalid content_type. url: /add/every, '
                     'content_type: {}'.format(request.content_type))
        return 'failed'
    every_name = request.json['every_name']
    start_date = change_timezone(request.json['start_time'])
    end_date = change_timezone(request.json['end_time'])
    notice = request.json['notice']
    repeat_type = request.json['repeat_type']
    dbcon.add_every(every_name, start_date, end_date, notice, repeat_type)
    return 'succeeded'


@app.route('/delete/task', methods=['POST'])
def delete_task():
    if request.content_type != 'application/json; charset=utf-8':
        logger.debug('err invalid content_type. url: /delete/task, '
                     'content_type: '
                     '{}'.format(request.content_type))
        return 'failed'
    # task_name = request.json['task_name']
    # due_date = change_timezone(request.json['due_date'])
    # dbcon.delete_task(task_name, due_date)
    object_id = request.json['_id']
    dbcon.delete_task(object_id)
    return 'succeeded'


@app.route('/delete/schedule', methods=['POST'])
def delete_schedule():
    if request.content_type != 'application/json; charset=utf-8':
        logger.debug('err invalid content_type. url: /delete/schedule, '
                     'content_type: {}'.format(request.content_type))
        return 'failed'
    # schedule_name = request.json['schedule_name']
    # start_date = change_timezone(request.json['start_date'])
    # dbcon.delete_schedule(schedule_name, start_date)
    object_id = request.json['_id']
    dbcon.delete_schedule(object_id)
    return 'succeeded'


@app.route('/delete/every', methods=['POST'])
def delete_every():
    if request.content_type != 'application/json; charset=utf-8':
        logger.debug('err invalid content_type. url: /delete/every, '
                     'content_type: {}'.format(request.content_type))
        return 'failed'
    # chedule_name = request.json['every_name']
    # start_date = change_timezone(request.json['start_date'])
    object_id = request.json['_id']
    dbcon.delete_every(object_id)
    return 'succeeded'


@app.route('/update/task', methods=['POST'])
def update_task():
    if request.content_type != 'application/json; charset=utf-8':
        logger.debug('err invalid content_type. url: /update/task, '
                     'content_type: {}'.format(request.content_type))
        return 'failed'
    data = request.json
    if '_id' not in data:
        print("none object_id")
        return 'not succeeded task update'
    update_items = {}
    if 'task_name' in data:
        update_items['task_name'] = data['task_name']
    if 'due_date' in data:
        update_items['due_date'] = strtime.str_to_datetime(data['due_date'])
    if 'task_type' in data:
        update_items['task_type'] = data['task_type']
    if 'guide_time' in data:
        update_items['task_type'] = data['task_type']
    if 'progress' in data:
        update_items['progress'] = data['progress']
    if 'priority' in data:
        update_items['priority'] = data['priority']
    print(update_items)
    dbcon.update_task(data['_id'], update_items)
    # task_name = data['task_name'] if 'task_name' in data else None
    # due_date = change_timezone(data['due_date']) if 'due_date' in data else None
    # task_type = data['task_type'] if 'task_type' in data else None
    # guide_time = strtime.str_to_time(data['guide_time']) if 'guide_time' in \
    #                 data else None
    # progress = data['progress'] if 'progress' in data else None
    # priority = data['priority'] if 'priority' in data else None
    # dbcon.update_task(object_id, task_name, due_date, task_type, guide_time,
    # progress,
    # priority)
    return 'succeeded'


@app.route('/update/schedule', methods=['POST'])
def update_schedule():
    if request.content_type != 'application/json; charset=utf-8':
        logger.debug('err invalid content_type. url: /update/schedule, '
                     'content_type: {}'.format(request.content_type))
        return 'failed'
    data = request.json
    if '_id' not in data:
        return 'not succeeded schedule data'
    update_items = {}
    if 'schedule_name' in data:
        update_items['schedule_name'] = data['schedule_name']
    if 'start_date' in data:
        update_items['start_date'] = strtime.str_to_datetime(data['start_date'])
    if 'end_date' in data:
        update_items['end_date'] = strtime.str_to_datetime(data['end_date'])
    dbcon.update_schedule(data['_id'], update_items)
    # schedule_name = data['schedule_name'] if 'schedule_name' in data else None
    # start_date = change_timezone(data['start_date']) if 'start_date' in data \
    #                 else None
    # end_date = change_timezone(data['end_date']) if 'end_date' in data else None
    # notice = data['notice'] if 'notice' in data else None
    # dbcon.update_schedule(schedule_name, start_date, end_date, notice)
    return 'succeeded'


@app.route('/update/every', methods=['POST'])
def update_every():
    if request.content_type != 'application/json; charset=utf-8':
        logger.debug('err invalid content_type. url: /update/every, '
                     'content_type: {}'.format(request.content_type))
        return 'failed'
    data = request.json
    if '_id' not in data:
        return 'not succeeded every update'
    update_items = {}
    if 'every_name' in data:
        update_items['every_name'] = data['every_name']
    if 'start_date' in data:
        update_items['start_date'] = strtime.str_to_datetime(data['start_date'])
    if 'end_date' in data:
        update_items['end_date'] = strtime.str_to_datetime(data['end_date'])
    dbcon.update_every(data['_id'], update_items)
    # schedule_name = data['every_name'] if 'every_name' in data else None
    # start_date = change_timezone(data['start_date']) if 'start_date' in data \
    #                 else None
    # end_date = change_timezone(data['end_date']) if 'end_date' in data else None
    # notice = data['notice'] if 'notice' in data else None
    # repeat_type = data['repeat_type'] if 'repeat_type' in data else None
    # dbcon.update_every(schedule_name, start_date, end_date, notice, repeat_type)
    return 'succeeded'


@app.route('/get/todo_list', methods=['GET'])
def get_todo_list():
    global friend_flag
    data = dbcon.get_todo_list()
    if friend_flag:
        data["todo_list"].append({"friend": friend_flag})
        friend_flag = False
    else:
        data["friend"] = friend_flag
    return json.dumps(data)


@app.route('/friend/get/todo_list', methods=['GET'])
def friend_get_todo_list():
    return dbcon.get_todo_list()


@app.route('/friend/update/task', methods=['POST'])
def friend_update_task():
    global friend_flag
    if request.content_type != 'application/json; charset=utf-8':
        logger.debug('err invalid content_type. url: /update/task, '
                     'content_type: {}'.format(request.content_type))
        return 'failed'
    data = request.json
    if 'object_id' not in data:
        return 'not succeeded task update'
    update_items = {}
    if 'task_name' in data:
        update_items['task_name'] = data['task_name']
    if 'due_date' in data:
        update_items['due_date'] = data['due_date']
    if 'task_type' in data:
        update_items['task_type'] = data['task_type']
    if 'guide_time' in data:
        update_items['task_type'] = data['task_type']
    if 'progress' in data:
        update_items['progress'] = data['progress']
    if 'priority' in data:
        update_items['priority'] = data['priority']
    dbcon.update_task(data['object_id'], update_items)
    # task_name = data['task_name'] if 'task_name' in data else None
    # due_date = change_timezone(data['due_date']) if 'due_date' in data else None
    # task_type = data['task_type'] if 'task_type' in data else None
    # guide_time = strtime.str_to_time(data['guide_time']) if 'guide_time' in \
    #                 data else None
    # progress = data['progress'] if 'progress' in data else None
    # priority = data['priority'] if 'priority' in data else None
    # dbcon.update_task(object_id, task_name, due_date, task_type, guide_time,
    # progress,
    # priority)
    friend_flag = True
    return 'succeeded'


@app.route('/friend/update/schedule', methods=['POST'])
def friend_update_schedule():
    if request.content_type != 'application/json; charset=utf-8':
        logger.debug('err invalid content_type. url: /update/schedule, '
                     'content_type: {}'.format(request.content_type))
        return 'failed'
    data = request.json
    if 'object_id' not in data:
        return 'not succeeded schedule data'
    update_items = {}
    if 'schedule_name' in data:
        update_items['schedule_name'] = data['schedule_name']
    if 'start_date' in data:
        update_items['start_date'] = data['start_date']
    if 'end_date' in data:
        update_items['end_date'] = data['end_date']
    dbcon.update_schedule(data['object_id'], update_items)
    # schedule_name = data['schedule_name'] if 'schedule_name' in data else None
    # start_date = change_timezone(data['start_date']) if 'start_date' in data \
    #                 else None
    # end_date = change_timezone(data['end_date']) if 'end_date' in data else None
    # notice = data['notice'] if 'notice' in data else None
    # dbcon.update_schedule(schedule_name, start_date, end_date, notice)
    friend_flag = True
    return 'succeeded'


@app.route('/friend/update/every', methods=['POST'])
def friend_update_every():
    global friend_flag
    if request.content_type != 'application/json; charset=utf-8':
        logger.debug('err invalid content_type. url: /update/every, '
                     'content_type: {}'.format(request.content_type))
        return 'failed'
    data = request.json
    if '_id' not in data:
        return 'not succeeded every update'
    update_items = {}
    if 'every_name' in data:
        update_items['every_name'] = data['every_name']
    if 'start_date' in data:
        update_items['start_date'] = data['start_date']
    if 'end_date' in data:
        update_items['end_date'] = data['end_date']
    dbcon.update_every(data['object_id'], update_items)
    # schedule_name = data['every_name'] if 'every_name' in data else None
    # start_date = change_timezone(data['start_date']) if 'start_date' in data \
    #                 else None
    # end_date = change_timezone(data['end_date']) if 'end_date' in data else None
    # notice = data['notice'] if 'notice' in data else None
    # repeat_type = data['repeat_type'] if 'repeat_type' in data else None
    # dbcon.update_every(schedule_name, start_date, end_date, notice, repeat_type)
    friend_flag = True
    return 'succeeded'


@app.route('/get/calendar')
def get_calendar():
    return dbcon.get_calendar()


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=80)
    # app.run(debug=True, port=3000)
