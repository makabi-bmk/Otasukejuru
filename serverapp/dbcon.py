import datetime as dt
from pytz import timezone
from logging import getLogger, StreamHandler, DEBUG
from pymongo import MongoClient
import json
from bson import ObjectId

# from serverapp.defineclass import Task, Schedule, Every

logger = getLogger(__name__)
handler = StreamHandler()
handler.setLevel(DEBUG)
logger.setLevel(DEBUG)
logger.addHandler(handler)
logger.propagate = False

client = MongoClient('localhost', 27017)
db = client['muffin']
every_col = db['every']
schedule_col = db['schedule']
friend_col = db['friend']
task_col = db['task']
task_count = 0

res_list = []
task_list = []
schedule_list = []
every_list = []


def add_task(task_name: str, due_date: dt.datetime, task_type: int,
             guide_time: dt.datetime, progress: int, priority: int) -> None:
    post = {
        "task_name": task_name,
        "registration_date": dt.datetime.now(timezone('Asia/Tokyo')),
        "due_date": due_date,
        "task_type": task_type,
        "guide_time": guide_time,
        "progress": progress,
        "priority": priority,
        "friend": False
    }
    task_col.insert_one(post)
    new_task_list()


def add_schedule(schedule_name: str, start_time: dt.datetime,
                 end_time: dt.datetime) -> bool:
    flg = True
    for i in schedule_col.find():
        if start_time <= i["start_time"] <= end_time:
            flg = False
        if start_time <= i["end_time"] <= end_time:
            flg = False
        if i["start_time"] <= start_time <= i["end_time"]:
            flg = False

    if flg:
        post = {
            "schedule_name": schedule_name,
            "start_time": start_time,
            "end_time": end_time,
        }
        schedule_col.insert(post)
        new_schedule_list()
        return json.dumps(True)
    else:
        return json.dumps(False)


def add_every(name: str, start_time: dt.datetime, end_time: dt.datetime,
              notice: int, repeat_type: int) -> None:
    post = {
        "name": name,
        "start_time": start_time,
        "end_time": end_time,
        "notice": notice,
        "repeat_type": repeat_type,
    }
    every_col.insert_one(post)
    new_every_list()


def delete_task(object_id):
    task_col.remove({"_id": ObjectId(object_id)})


def delete_schedule(object_id):
    schedule_col.remove({"_id": ObjectId(object_id)})


def delete_every(object_id):
    every_col.remove({"_id": ObjectId(object_id)})


def update_task(object_id: str, update_items: dict, friend_flag=False):
    object_id = ObjectId(object_id)
    if friend_flag:
        update_items['friend'] = True
    task_col.update({"_id": object_id}, {'$set': update_items}, upsert=True)
    new_task_list()


def update_schedule(object_id: str, update_items: dict, friend_flag=False):
    object_id = ObjectId(object_id)
    if friend_flag:
        update_items['friend'] = True

    flg = True
    for i in schedule_col.find({"_id": {"$ne": object_id}}):
        if update_items["start_time"] <= i["start_time"] <= update_items["end_time"]:
            flg = False
        if update_items["start_time"] <= i["end_time"] <= update_items["end_time"]:
            flg = False
        if i["start_time"] <= update_items["start_time"] <= i["end_time"]:
            flg = False

    if flg:
        schedule_col.update({"_id": object_id}, {"$set": update_items},
                            upsert=True)
        new_schedule_list()
        return True
    else:
        return False


def update_every(object_id: str, update_items: dict, friend_flag=False):
    object_id = ObjectId(object_id)
    if friend_flag:
        update_items['friend'] = True
    every_col.update({"_id": object_id}, {'$set': update_items}, upsert=True)
    new_every_list()


def set_priority(n_banme):
    global task_count
    if task_count <= 10:
        if n_banme <= 0:
            return 0
        elif n_banme <= 2:
            return 1
        elif n_banme <= 5:
            return 2
        else:
            return 3
    else:
        if n_banme <= 2:
            return 0
        elif n_banme <= 7:
            return 1
        elif n_banme <= 17:
            return 2
        else:
            return 3


def get_todo_list():
    global task_count
    todo_list = []
    now = dt.datetime.now()
    task_count = len(list(task_col.find()))
    for i, task in enumerate(sorted(task_col.find(), key=lambda x: -(100 - x["progress"]) / (x["due_date"] - now).total_seconds() / 3600 + 1)):
        task["_id"] = str(task["_id"])
        if "registration_date" in task:
            task["registration_date"] = str(task["registration_date"])
        task["due_date"] = str(task["due_date"])
        task["guide_time"] = str(task["guide_time"])

        task["priority"] = set_priority(i)

        todo_list.append(task)
    return {"todo_list": todo_list}


def get_calendar():
    global schedule_list
    global every_list
    global task_list
    if schedule_list == []:
        new_schedule_list()
    if every_list == []:
        new_every_list()
    if task_list == []:
        new_every_list()
    calendar = {
        "schedule": schedule_list,
        "every": every_list,
        "task": task_list,
        "friend": []
    }
    return json.dumps(calendar)


def new_task_list():
    global task_count
    global task_list
    task_list = []
    now = dt.datetime.now()
    task_count = len(list(task_col.find()))
    for i, task in enumerate(sorted(task_col.find(), key=lambda x: -(100 - x["progress"]) / (x["due_date"] - now).total_seconds() / 3600 + 1)):
        task["_id"] = str(task["_id"])
        if "registration_date" in task:
            task["registration_date"] = str(task["registration_date"])
        task["due_date"] = str(task["due_date"])
        task["guide_time"] = str(task["guide_time"])
        task["priority"] = set_priority(i)
        task_list.append(task)


def new_schedule_list():
    global schedule_list
    schedule_list = []
    for i in schedule_col.find():
        i["_id"] = str(i["_id"])
        i["start_time"] = str(i["start_time"])
        i["end_time"] = str(i["end_time"])
        schedule_list.append(i)


def new_every_list():
    global every_list
    every_list = []
    for i in every_col.find():
        i["_id"] = str(i["_id"])
        i["start_time"] = str(i["start_time"])
        i["end_time"] = str(i["end_time"])
        every_list.append(i)


def add_sub_task(object_id, sub_task_name, sub_task):
    data = task_col.find_one({'_id': ObjectId(object_id)})
    data.update({"sub_task": {sub_task_name: sub_task}})
    del(data['_id'])
    task_col.update({'_id': ObjectId(object_id)}, {'$set': data}, upsert=True)


if __name__ == '__main__':
    pass
