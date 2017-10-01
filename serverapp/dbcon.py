import datetime as dt
from pytz import timezone
from logging import getLogger, StreamHandler, DEBUG
from pymongo import MongoClient

# from serverapp.defineclass import Task, Schedule, Every

logger = getLogger(__name__)
handler = StreamHandler()
handler.setLevel(DEBUG)
logger.setLevel(DEBUG)
logger.addHandler(handler)
logger.propagate = False

client = MongoClient('localhost', 27017)
every_col = client['every']
schedule_col = client['schedule']
task_col = client['task']

res_list = []


def get_schedule_list():
    for data in schedule_col:
        del (data["_id"])
        res_list.append(data)
    for data in every_col:
        del (data["_id"])
        res_list.append(data)
    res_list.sort(key=lambda x, y: x["start_time"] < y["end_time"])
    # TODO: 優先度順にタスクを詰めていく


def add_task(task_name: str, due_date: dt.datetime, repeat: int, task_type: int,
             guide_time: dt.datetime, progress: int,  priority: int) -> None:
    post = {
        "task_name": task_name,
        "registration_date": dt.datetime.now(timezone('Asia/Tokyo')),
        "due_date": due_date,
        "repeat": repeat,
        "task_type": task_type,
        "guide_time": guide_time,
        "progress": progress,
        "priority": priority,
        "run_time": 0
    }
    task_col.insert_one(post)


# 3日以上のスケジュール死
def add_schedule(schedule_name: str, start_time: dt.datetime,
                 end_time: dt.datetime) -> None:
    res = check_2day(start_time, end_time)
    if res["result"]:
        before = {
            "schedule_name": schedule_name,
            "start_time": res["before_day"][0],
            "end_time": res["before_day"][1]
        }
        after = {
            "schedule_name": schedule_name,
            "start_time": res["after_day"][0],
            "end_time": res["after_day"][1]
        }
        schedule_col.insert_one(before)
        schedule_col.insert_one(after)
    else:
        post = {
            "schedule_name": schedule_name,
            "start_time": res["day"][0],
            "end_time": res["day"][1]
        }
        schedule_col.insert_one(post)


def make_zero_time(time: dt.datetime) -> dt.timedelta:
    return dt.timedelta(hours=time.hour, minutes=time.minute,
                        seconds=time.second)


def add_day(dist: int) -> dt.timedelta:
    return dt.timedelta(days=dist)


def check_2day(start_time: dt.datetime, end_time: dt.datetime):
    if start_time.day != end_time.day:
        res = {
            "result": True,
            "before_day": [start_time, end_time - make_zero_time(end_time)],
            "after_day": [end_time - make_zero_time(end_time) + add_day(1),
                          end_time]
        }
    else:
        res = {
            "result": False,
            "day": [start_time, end_time]
        }
    return res


def add_every(name: str, repeat_type: int, start_time: dt.datetime,
              end_time: dt.datetime) -> None:
    post = {
        "name": name,
        "repeat_type": repeat_type,
        "start_time": start_time,
        "end_time": end_time,
    }
    every_col.insert_one(post)
