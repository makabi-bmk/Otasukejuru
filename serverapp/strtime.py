import datetime
import re


reg_time = re.compile("(\d){2}[^- ](\d){2}[^- ](\d){2}")
reg_date = re.compile("(\d){4}.(\d){2}.(\d){2}")
reg_dtime = re.compile("(\d){4}.(\d){2}.(\d){2}.(\d){2}.(\d){2}.(\d){2}")


def str_to_time(time: str):
    """
    :param time: "%H:%m:%d
    :return: datetime.datetime
    """
    time = reg_time.search(time).group()
    return datetime.datetime.strptime(time, "%H:%M:%S")


def str_to_date(date: str):
    """
    :param date: "%Y-%m-%d"
    :return: datetime.datetime
    """
    date = reg_date.search(date).group()
    return datetime.datetime.strptime(date, "%Y-%m-%d")


def str_to_datetime(dtime: str):
    """
    :param dtime: "%Y-%m-%d %H:%M:%S"
    :return:
    """
    dtime = reg_dtime.search(dtime).group()
    return datetime.datetime.strptime(dtime, "%Y-%m-%d %H:%M:%S")


if __name__ == '__main__':
    print(str_to_time(str(datetime.datetime.now())))
    print(str_to_date(str(datetime.datetime.now())))
    print(str_to_datetime(str(datetime.datetime.now())))
