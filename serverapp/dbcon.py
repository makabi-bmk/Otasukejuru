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

res_list = []


# def get_schedule_list():
#     for data in schedule_col:
#         del (data["_id"])
#         res_list.append(data)
#     for data in every_col:
#         del (data["_id"])
#         res_list.append(data)
#     res_list.sort(key=lambda x, y: x["start_time"] < y["end_time"])
#     # TODO: 優先度順にタスクを詰めていく


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
        # "run_time": 0
    }
    print(post)
    task_col.insert_one(post)


def add_schedule(schedule_name: str, start_date: dt.datetime,
                 end_date: dt.datetime, notice: int) -> None:
    res = check_days(start_date, end_date)
    # 複数スケジュールだった場合、分割
    if res["result"]:
        for i, day in enumerate(res["days"]):
            # 連日スケジュールの初日だけ通知
            if i == 0:
                post = {
                    "schedule_name": schedule_name,
                    "start_time": day[0],
                    "end_time": day[1],
                    "notice": notice
                }
            else:
                post = {
                    "schedule_name": schedule_name,
                    "start_time": day[0],
                    "end_time": day[1],
                }
            print(post)
            schedule_col.insert_one(post)
    else:
        post = {
            "schedule_name": schedule_name,
            "start_time": res["day"][0],
            "end_time": res["day"][1],
            "notice": notice
        }
        print(post)
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
    print(post)
    every_col.insert_one(post)


def delete_task(task_name: str, start_date: dt.datetime):
    task_col.remove({"task_name": task_name, "start_time": start_date})


def delete_schedule(schedule_name: str, start_date: dt.datetime):
    task_col.remove({"schedule_name": schedule_name, "start_time": start_date})


def delete_every(every_name: str, start_date: dt.datetime):
    task_col.remove({"every_name": every_name, "start_time": start_date})


def update_task(object_id: str, update_items: dict, friend_flag=False):
    object_id = ObjectId(object_id)
    if friend_flag:
        update_items['friend'] = True
    task_col.update({"_id": object_id}, {'$set': {update_items}}, upsert=True)


def update_schedule(object_id: str, update_items: dict, friend_flag=False):
    object_id = ObjectId(object_id)
    if friend_flag:
        update_items['friend'] = True
    schedule_col.update({"_id": object_id}, {"$set": {update_items}},
                        upsert=True)


def update_every(object_id: str, update_items: dict, friend_flag=False):
    object_id = ObjectId(object_id)
    if friend_flag:
        update_items['friend'] = True
    every_col.update({"_id": object_id}, {'$set': {update_items}}, upsert=True)


def get_todo_list():
    todo_list = []
    for task in task_col.find():
        task["_id"] = str(task["_id"])
        task["registration_date"] = str(task["registration_date"])
        task["due_date"] = str(task["due_date"])
        task["guide_time"] = str(task["guide_time"])
        todo_list.append(task)
    logger.debug("{} | get_todo_list: {}".format(dt.datetime.now(), todo_list))
    return json.dumps({"todo_list": todo_list})


def get_calendar():
    s = []
    e = []
    f = []
    t = []
    for i in schedule_col.find():
        i["_id"] = ObjectId(i["_id"])
        i["start_date"] = str(i["start_date"])
        i["end_date"] = str(i["end_date"])
        s.append(i)
    for i in every_col.find():
        i["_id"] = ObjectId(i["_id"])
        i["start_date"] = str(i["start_date"])
        i["end_date"] = str(i["end_date"])
        e.append(i)
    for i in task_col.find({"friend": True}):
        i["_id"] = ObjectId(i["_id"])
        i["due_date"] = str(i["due_date"])
        i["guide_time"] = str(i["guide_time"])
        i["registration_date"] = str(i["registration_date"])
        f.append(i)
    for i in task_col.find({"friend": {"$exists": False}}):
        i["_id"] = ObjectId(i["_id"])
        i["due_date"] = str(i["due_date"])
        i["guide_time"] = str(i["guide_time"])
        i["registration_date"] = str(i["registration_date"])
        t.append(i)
    calendar = {
        "schedule": s,
        "every": e,
        "friend": f,
        "task": [i.pop("_id") for i in task_col.find(
            {"friend": {"$exists": False}}).sort(
                {"priority": -1},
                {"progress": 1},
                {"due_date": 1}
            )
        ]
    }
    return json.dumps(calendar)


if __name__ == '__main__':
    a = dt.datetime.now()
    r = check_days(a, a + dt.timedelta(days=1))
    print(r)
    a = dt.datetime.now()
    r = check_days(a, a + dt.timedelta(days=0))
    print(r)
