--
-- Structure for table shoppingcart_shopping_item
--
DROP TABLE IF EXISTS shoppingcart_shopping_item;
CREATE TABLE shoppingcart_shopping_item (		
	id_item int(11) NOT NULL default '0',
	id_provider varchar(255) NOT NULL default '',
	id_lot int(11) NOT NULL default '0',
	id_user int(11) NOT NULL default '0',
	resource_type varchar(255) NOT NULL default '',
	id_resource varchar(255) NOT NULL default '',
	item_price double NOT NULL default '0',
	PRIMARY KEY (id_item)
);
