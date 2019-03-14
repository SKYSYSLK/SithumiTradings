CREATE table invoiceItems(
  item_id varchar(8),
  invoice_id varchar(20),
  FOREIGN KEY(item_id) REFERENCES items(id) ON DELETE cascade,
  FOREIGN KEY(invoice_id) REFERENCES invoices(id) ON DELETE cascade
)