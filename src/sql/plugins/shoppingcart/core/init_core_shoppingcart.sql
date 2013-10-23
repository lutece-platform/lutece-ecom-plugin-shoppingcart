
--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'UPDATER_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('SHOPPINGCART_MANAGEMENT','shoppingcart.adminFeature.ManageShoppingCart.name',1,'jsp/admin/plugins/shoppingcart/ManageShoppingCart.jsp','shoppingcart.adminFeature.ManageShoppingCart.description',0,'shoppingcart',NULL,NULL,NULL,4);


--
-- Data for table core_user_right
--
INSERT INTO core_user_right (id_right,id_user) VALUES ('SHOPPINGCART_MANAGEMENT',1);

INSERT INTO core_datastore (entity_key,entity_value) VALUES ('shoppingcart.nbHoursBeforeCleaning', '48');
INSERT INTO core_datastore (entity_key,entity_value) VALUES ('shoppingcart.urlBackAfterValidation', 'jsp/site/Portal.jsp');
