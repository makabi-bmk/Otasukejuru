import random
import datetime as dt


class Task:
    def __init__(self, task_name: str, registration_date: dt.datetime,
                 due_date: dt.datetime, repeat: int, task_type: int,
                 guide_time: dt.datetime, progress: int, priority: int):
        self.mongo_id
        self.task_name: str = task_name
        self.registration_date: dt.datetime = registration_date  # 登録日
        self.due_date: dt.datetime = due_date  # 期限
        self.repeat: int = repeat
        self.task_type: int = task_type
        self.guide_time: dt.datetime = guide_time
        # self.run_time: int = run_time  # 作業時間(単位(時間))
        self.progress: int = progress
        self.priority: int = priority


class Schedule:
    def __init__(self, schedule_name: str, date: dt.datetime):
        self.schedule_name: str = schedule_name
        self.date = date


class Every:
    def __init__(self, name: str, start_time: dt.datetime,
                 end_time: dt.datetime):
        self.name: str = name
        self.start_time: dt.datetime = start_time
        self.end_time: dt.datetime = end_time


if __name__ == '__main__':
    l = [Task(str(i), dt.datetime.now(), dt.datetime.now(), i % 2, i * 10) for i
         in range(10)]
    random.shuffle(l)
    s = sorted(l, key=lambda x: (-x.priority, x.progress, x.due_date))
    for i in s:
        print(
            "x: {}, due_date: {}, priority: {}".format(i.task_name, i.due_date,
                                                       i.priority))
