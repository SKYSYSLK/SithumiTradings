CREATE TABLE cheques(
  id varchar(50) PRIMARY KEY,
  amount decimal(8,2),
  bank varchar (30),
  issue_date date,
  expire_date date,
  branch varchar(50),
  status int(1)
)