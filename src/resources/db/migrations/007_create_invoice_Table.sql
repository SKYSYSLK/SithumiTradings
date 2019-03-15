CREATE TABLE invoices(
  id varchar (20) PRIMARY KEY,
  shop_id int(11),
  amount decimal(8,2),
  date_issued date,
  cheque_id varchar(50),
  type int(1) DEFAULT(1),
  FOREIGN KEY(cheque_id) REFERENCES cheques(id) ON DELETE cascade ,
  FOREIGN key(shop_id) REFERENCES shops(id) ON DELETE cascade
)