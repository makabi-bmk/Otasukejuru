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


def add_schedule(schedule_name: str, start_date: dt.datetime,
                 end_date: dt.datetime) -> None:
    res = check_days(start_date, end_date)
    # 複数スケジュールだった場合、分割
    # もう分割しなくていい
    # if res["result"]:
    #     for i, day in enumerate(res["days"]):
    #         # 連日スケジュールの初日だけ通知
    #         print("add_schedule: now day", i)
    #         if i == 0:
    #             post = {
    #                 "schedule_name": schedule_name,
    #                 "start_time": day[0],
    #                 "end_time": day[1],
    #                 "notice": notice
    #             }
    #         else:
    #             post = {
    #                 "schedule_name": schedule_name,
    #                 "start_time": day[0],
    #                 "end_time": day[1],
    #             }
    #         print(post)
    #         schedule_col.insert_one(post)
    # else:
    post = {
        "schedule_name": schedule_name,
        "start_time": start_date,
        "end_time": end_date,
    }
    schedule_col.insert_one(post)


def make_zero_time(date: dt.datetime) -> dt.datetime:
    return date - dt.timedelta(hours=date.hour, minutes=date.minute,
                               seconds=date.second)


def make_last_time(date: dt.datetime) -> dt.datetime:
    return date + dt.timedelta(hours=23 - date.hour, minutes=59 - date.minute,
                               seconds=59 - date.second)


def add_day(date: dt.datetime, dist: int) -> dt.datetime:
    return date + dt.timedelta(days=dist)


def check_days(start_time: dt.datetime, end_time: dt.datetime):
    if start_time.day != end_time.day:
        res = {
            "result": True,
            "days": []
        }
        zero_time = make_zero_time(start_time)
        last_time = make_last_time(start_time)
        res['days'].append([start_time, last_time])
        for i in range(end_time.day - start_time.day - 1):
            res['days'].append([add_day(zero_time, i + 1),
                                add_day(last_time, i + 1)])
        res['days'].append([make_zero_time(end_time), end_time])
    else:
        res = {
            "result": False,
            "day": [start_time, end_time]
        }
    return res


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
    # print(update_items)
    task_col.update({"_id": object_id}, {'$set': update_items}, upsert=True)


def update_schedule(object_id: str, update_items: dict, friend_flag=False):
    object_id = ObjectId(object_id)
    if friend_flag:
        update_items['friend'] = True
    schedule_col.update({"_id": object_id}, {"$set": update_items},
                        upsert=True)


def update_every(object_id: str, update_items: dict, friend_flag=False):
    object_id = ObjectId(object_id)
    if friend_flag:
        update_items['friend'] = True
    every_col.update({"_id": object_id}, {'$set': update_items}, upsert=True)


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
    # print(task_count)
    for i, task in enumerate(sorted(task_col.find(), key=lambda x: -(100 - x["progress"]) / (x["due_date"] - now).total_seconds() / 3600 + 1)):
        task["_id"] = str(task["_id"])
        task["registration_date"] = str(task["registration_date"])
        task["due_date"] = str(task["due_date"])
        task["guide_time"] = str(task["guide_time"])

        task["priority"] = set_priority(i)
        # print(i, task["priority"])

        todo_list.append(task)
    logger.debug("{} | get_todo_list: {}".format(dt.datetime.now(), todo_list))
    return {"todo_list": todo_list}


def get_calendar():
    s = []
    e = []
    f = []
    t = []
    for i in schedule_col.find():
        i["_id"] = str(i["_id"])
        i["start_time"] = str(i["start_time"])
        i["end_time"] = str(i["end_time"])
        s.append(i)
    for i in every_col.find():
        i["_id"] = str(i["_id"])
        i["start_time"] = str(i["start_time"])
        i["end_time"] = str(i["end_time"])
        e.append(i)
    for i in task_col.find({"friend": True}):
        i["_id"] = str(i["_id"])
        i["due_date"] = str(i["due_date"])
        i["guide_time"] = str(i["guide_time"])
        i["registration_date"] = str(i["registration_date"])
        f.append(i)
    for i in task_col.find({"friend": {"$exists": False}}):
        i["_id"] = str(i["_id"])
        i["due_date"] = str(i["due_date"])
        i["guide_time"] = str(i["guide_time"])
        i["registration_date"] = str(i["registration_date"])
        t.append(i)
    calendar = {
        "schedule": s,
        "every": e,
        "friend": f,
        "task": sorted(t, key=lambda x: (-x["priority"], x["progress"]))
    }
    return json.dumps(calendar)


if __name__ == '__main__':
    a = dt.datetime.now()
    r = check_days(a, a + dt.timedelta(days=1))
    print(r)
    a = dt.datetime.now()
    r = check_days(a, a + dt.timedelta(days=0))
    print(r)

