package DOT

object DaysOfTheWeek:
  object Mon
  object Tue
  object Wed
  object Thu
  object Fri
  object Sat
  object Sun

  type Weekend = Sat.type | Sun.type
  type Workweek = Mon.type | Tue.type | Wed.type | Thu.type | Fri.type
  type All = Weekend | Workweek
