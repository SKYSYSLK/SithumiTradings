CREATE TABLE item_buy(
  item_id varchar(10),
  quantity int,
  buyPrice DECIMAL(7,2),
  sellPrice DECIMAL(7,2),
  day Date,
  total_bill DECIMAL(10,2),
  payment_date Date,
  check_no varchar(25)
)