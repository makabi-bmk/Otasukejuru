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

# client = MongoClient('localhost', 27017)
# every_col = client['every']
# schedule_col = client['schedule']
# task_col = client['task']

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
    # task_col.insert_one(post)


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
            # schedule_col.insert_one(post)
    else:
        post = {
            "schedule_name": schedule_name,
            "start_time": res["day"][0],
            "end_time": res["day"][1],
            "notice": notice
        }
        print(post)
        # schedule_col.insert_one(post)


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


def add_every(name: str, repeat_type: int, start_time: dt.datetime,
              end_time: dt.datetime, repeat: int) -> None:
    post = {
        "name": name,
        "repeat_type": repeat_type,
        "start_time": start_time,
        "end_time": end_time,
        "repeat": repeat
    }
    print(post)
    # every_col.insert_one(post)


if __name__ == '__main__':
    a = dt.datetime.now()
    r = check_days(a, a + dt.timedelta(days=1))
    print(r)
    a = dt.datetime.now()
    r = check_days(a, a + dt.timedelta(days=0))
    print(r)
