INSERT INTO languages(id, code)
VALUES(1, 'ua'),(2, 'en'), (3, 'ru');

INSERT INTO users (id,
                   date_of_registration,
                   email,
                   email_notification,
                   name,
                   last_activity_time,
                   role,
                   user_status,
                   refresh_token_key)
VALUES (1, current_date, 'foo@bar.com', 1, 'foo', current_date, 1, 1, 'quux');
INSERT INTO eco_news(id,creation_date, image_path, author_id, text, title)
VALUES (1,'2020-04-11 18:33:51', 'шлях до картинки', 1,
        'No matter where you live , you can make a difference in the impact of big agriculture. Purchasing foods produced by small, local farms, opting for organic produce whenever possible',
        'A New Way To Buy Food'),
       (2, '2020-04-11 18:55:18', 'шлях до картинки', 1,
        'The benefits of biodegradable substances are only felt when they are disposed of properly. Compost piles capture and return all of the recycled nutrients to the environment, and help to sustain new life. ',
        'Why Biodegradable Products are Better for the Planet'),
       (3,'2020-04-11 19:06:36', 'шлях до картинки', 1,
        'Over six gallons of water are required to produce one gallon of wine.',
        'Sustainable Wine Is Less Damaging to the Environment, But How Can You Spot It?'),
       (4,'2020-04-11 19:14:15', 'шлях до картинки', 1,
        'Instead of trying to get rid of those lawn and garden weeds, harvest them for free homegrown meals.',
        'Please eat the dandelions: 9 edible garden weeds'),
       (5,'2020-04-11 19:22:57', 'шлях до картинки', 1,
        'Weather happens hour by hour, day by day—it''s a thunderstorm, a heat wave, a windy afternoon. Taken as averages over decades and centuries, those patterns of precipitation, temperature, and wind for a given region comprise our climate.',
        'Climate Change'),
       (6,'2020-04-11 19:31:35', 'шлях до картинки', 1,
        'According to the Environmental Protection Agency, food waste in the United States has tripled since 1960. In landfills, its decomposition generates methane, a potent greenhouse gas.',
        'A Growing Problem'),
       (7,'2020-04-11 19:44:19', 'шлях до картинки', 1,
        'Global warming — a component of climate change — is the rapid increase in recorded temperatures of the ocean, land, and air caused by rising levels of carbon dioxide and other greenhouse gases in the atmosphere.',
        'Global Warming'),
       (8,'2020-04-11 19:50:56', 'шлях до картинки', 1,
        'Researchers have found evidence of rainforests near the South Pole 90 million years ago, suggesting the climate was exceptionally warm at the time',
        'Traces of ancient rainforest in Antarctica point to a warmer prehistoric world'),
       (9,'2020-04-11 19:55:05', 'шлях до картинки', 1, 'Rising sea surface temperatures and acidic waters could eliminate nearly all existing coral reef habitats by 2100, suggesting restoration projects in these areas will likely meet serious challenges','Warming, acidic oceans may nearly eliminate coral reef habitats by 2100'),
       (10,'2020-04-11 20:12:56', 'шлях до картинки', 1, 'Four fossilized monkey teeth discovered deep in the Peruvian Amazon provide new evidence that more than one group of ancient primates journeyed across the Atlantic Ocean from Africa.', 'Ancient Teeth from Peru Hint Now-Extinct Monkeys Crossed Atlantic from Africa'),
       (11,'2020-04-11 20:15:03', 'шлях до картинки', 1, 'Researchers from Cambridge University and University of California San Diego have 3D printed coral-inspired structures that are capable of growing dense populations of microscopic algae', '3D-printed corals could improve bioenergy and help coral reefs');
INSERT INTO tags(id, type)
VALUES (1, 'ECO_NEWS'),(2, 'ECO_NEWS'),(3, 'ECO_NEWS');

INSERT INTO tag_translations(id, name, tag_id, language_id) VALUES(1, 'Novinu', 1, 1),
(2, 'News', 1, 2),(3, 'Novosti', 1, 3),(4, 'Osvita', 2, 1),(5, 'Education', 2, 2),
(6, 'Obrazovanie', 2, 3),(7, 'Reklama', 3, 1),(8, 'Ads', 3, 2),(9, 'Reklama', 3, 3);


INSERT INTO eco_news_tags(eco_news_id, tags_id)
VALUES (1, 3),
       (2, 3),
       (3, 1),
       (4, 3),
       (5, 1),
       (6, 1),
       (7, 1),
       (8, 1),
       (9, 1),
       (10, 1),
       (11, 1);