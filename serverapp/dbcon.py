import datetime as dt
from logging import getLogger, StreamHandler, DEBUG
from pymongo import MongoClient
from serverapp.defineclass import Task, Schedule

logger = getLogger(__name__)
handler = StreamHandler()
handler.setLevel(DEBUG)
logger.setLevel(DEBUG)
logger.addHandler(handler)
logger.propagate = False

client = MongoClient('localhost', '27017')
every_col = client['every']
schedule_col = client['schedule']
task_col = client['task']


def add_task(task_name: str, registration_date: dt.datetime,
             due_date: dt.datetime, priority: int):
    task = Task(task_name, registration_date, due_date, priority, run_time=0)
    post = {"task": task}
    # post = {
    #    "task_name": task_name,
    #    "registration_date": registration_date,
    #    "due_date": due_date,
    #    "priority": priority,
    #    "run_time": run_time
    # }
    task_col.insert_one(post)


def add_schedule(schedule_name: str, date: dt.datetime):
    schedule = Schedule(schedule_name, date)
    post = {"schedule": schedule}
    # post = {
    #    "schedule_name": schedule_name,
    #    "date": date
    # }
    task_col.insert_one(post)
