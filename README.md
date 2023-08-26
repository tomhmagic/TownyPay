# TownyPay

Created for my server TanukiCraft
play.tanukicraft.net
https://discord.gg/zd7pvJY9Zr

A plugin that pays Mayors and Kings on every new Towny day and create a daily budget that can be spent.

On my server I have disabled bank withdrawals to prevent rogue Mayors/Kings and to make them manage their finances. To allow Mayors/Kings to focus on growing their towns/nations TownyPay creates a daily pay for them.
To allow Mayors/Kings to pay other players, TownyPay introduces a pay command to pay from the town/nation bank within a daily budget.

For Towny Admins:
Mayor and King pay can be configured to be either fixed or a percentage of the town profit.
Profit is calculated as CurrentBalance – (Upkeep x ConfiguredNumber)

Commands and Permissions:

| First Header  | Second Header |
|`/town pay [resident] [amount]` | townypay.command.town.pay | Pays given resident the chosen amount from the town bank, if within budget.|
|`/town finance` | townypay.command.town.finance | Showes town budget information (Budget, Spend and Remaining)|

`/nation pay [resident] [amount]` | townypay.command.nation.pay | Pays given resident the chosen amount from the town bank, if within budget.
`/nation finance` | townypay.command.nation.finance | Showes nation budget information (Budget, Spend and Remaining)


