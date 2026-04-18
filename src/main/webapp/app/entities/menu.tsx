import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/user-profile">
        <Translate contentKey="global.menu.entities.userProfile" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/warehouse">
        <Translate contentKey="global.menu.entities.warehouse" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/product">
        <Translate contentKey="global.menu.entities.product" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/recipe">
        <Translate contentKey="global.menu.entities.recipe" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/ingredient">
        <Translate contentKey="global.menu.entities.ingredient" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/supplier">
        <Translate contentKey="global.menu.entities.supplier" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/dietary-entry">
        <Translate contentKey="global.menu.entities.dietaryEntry" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/allergy-intolerance">
        <Translate contentKey="global.menu.entities.allergyIntolerance" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
