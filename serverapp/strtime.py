import datetime


def str_to_time(time: str):
    """
    :param time: "%H:%m:%d
    :return: datetime.datetime
    """
    return datetime.datetime.strptime(time, "%H:%M:%S")


def str_to_date(date: str):
    """
    :param date: "%Y-%m-%d"
    :return: datetime.datetime
    """
    return datetime.datetime.strptime(date, "%Y-%m-%d")


def str_to_datetime(dtime: str):
    """
    :param dtime: "%Y-%m-%d %H:%M:%S"
    :return:
    """
    return datetime.datetime.strptime(dtime, "%Y-%m-%d %H:%M:%S")


if __name__ == '__main__':
    print(str_to_time(str(datetime.datetime.now())))
    print(str_to_date(str(datetime.datetime.now())))
    print(str_to_datetime(str(datetime.datetime.now())))
